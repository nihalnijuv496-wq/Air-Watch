package org.airwatch.project.WorldMap

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.airwatch.project.Aircraft.AircraftViewModel
import org.airwatch.project.Aircraft.AircraftViewModelFactory
import org.airwatch.project.Filter.FilterViewModel
import org.airwatch.project.WorldMap.AircraftDetails.AircraftDetailCard
import org.airwatch.project.WorldMap.AircraftDetails.drawAircrafts

@Composable
fun DrawMapCanvas(
    type: String,
    screenWidth: Int,
    screenHeight: Int,
    aircraftViewModel: AircraftViewModel = viewModel(factory = AircraftViewModelFactory),
    filerViewModel : FilterViewModel = viewModel())
{
    var coordinateTree by remember { mutableStateOf<Chunk?>(null) }
    var selectedAircraftIcao by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        coordinateTree = withContext(Dispatchers.IO) {
            loadMapToTree()
        }
    }
    val tree = coordinateTree ?: return

    val projection = remember(type, screenWidth, screenHeight)
    {
        if(type == "Plane") {
            EquirectangularProjection(
                viewportWidth = screenWidth.toDouble(),
                viewportHeight = screenHeight.toDouble()
            )
        }
        else
        {
            OrthographicProjection(
                viewportWidth = screenWidth.toDouble(),
                viewportHeight = screenHeight.toDouble(),
            )
        }
    }

    var isGestureActive by remember { mutableStateOf(false) }
    val lodThreshold = if (isGestureActive) 20.0 else 5.0

    val visiblePoints by remember(tree, projection, screenWidth, screenHeight) {
        derivedStateOf {
            buildList {
                tree.traverse(projection, screenWidth, screenHeight, lodThreshold) { node ->
                    node.coordinates.forEach { coordinate ->
                        projection.project(coordinate)?.let { add(it) }
                    }
                }
            }
        }
    }

    val showableAircrafts = aircraftViewModel.getShowableAircrafts(
        filerViewModel.isFiltering,
        filerViewModel.filteredAirCrafts
    )
    val visibleAircrafts by remember (
        filerViewModel.isFiltering,
        filerViewModel.filteredAirCrafts.toList(),
        showableAircrafts,
        projection,
        screenWidth,
        screenHeight
    )
    {
        derivedStateOf {
            buildMap {
                showableAircrafts
                    .filter { it.position.latitude != null && it.position.longitude != null }
                    .forEach { aircraft ->
                        val projectedPosition = projection.project(aircraft.position)
                        projectedPosition?.let { put(aircraft.icao24, it) }
                    }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .pointerInput(type) {
                    coroutineScope {
                        launch {
                            awaitEachGesture {
                                awaitFirstDown(requireUnconsumed = false)
                                isGestureActive = true
                                waitForUpOrCancellation()
                                isGestureActive = false
                            }
                        }
                        launch {
                            detectTransformGestures { _, pan, zoom, _ ->
                                projection.pan(pan)
                                projection.zoom(zoom)
                            }
                        }
                        // Handle tap detection for selecting nearest aircraft
                        launch {
                            detectTapGestures { tapOffset ->
                                val radiusPx = 24f
                                var bestIcao: String? = null
                                var bestDist = radiusPx

                                visibleAircrafts.forEach { (icao, screenPos) ->
                                    val dist = (screenPos - tapOffset).getDistance()
                                    if (dist <= bestDist) {
                                        bestDist = dist
                                        bestIcao = icao
                                    }
                                }
                                selectedAircraftIcao = bestIcao
                            }
                        }
                    }
                }
        ) {
            drawPoints(
                points = visiblePoints,
                pointMode = PointMode.Points,
                color = Color.Black,
                strokeWidth = 0.5f
            )
            drawAircrafts(visibleAircrafts)
        }

        // Render Detail Card when an aircraft is selected
        selectedAircraftIcao?.let { icao ->
            val selectedAircraft = showableAircrafts.find { it.icao24 == icao }
            selectedAircraft?.let { aircraft ->
                AircraftDetailCard(
                    aircraft = aircraft,
                    onDismiss = { selectedAircraftIcao = null },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }
    }
}