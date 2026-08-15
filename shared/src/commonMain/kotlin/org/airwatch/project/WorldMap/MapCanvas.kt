package org.airwatch.project.WorldMap

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.airwatch.project.Aircraft.AircraftViewModel
import org.airwatch.project.Aircraft.AircraftViewModelFactory
import org.airwatch.project.Aircraft.Coordinate
import org.airwatch.project.Filter.FilterViewModel
import org.airwatch.project.WorldMap.AircraftDrawer.drawAircraft

@Composable
fun DrawMapCanvas(type: String, screenWidth: Int, screenHeight: Int,
                  aircraftViewModel: AircraftViewModel = viewModel(factory = AircraftViewModelFactory),
                  filerViewModel : FilterViewModel = viewModel())
{
    var coordinateTree by remember { mutableStateOf<Chunk?>(null) }

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


    val visiblePoints by remember(tree, projection, screenWidth, screenHeight) {
        derivedStateOf {
            buildList {
                tree.traverse(projection, screenWidth, screenHeight) { node ->
                    node.coordinates.forEach { coordinate ->
                        projection.project(coordinate)?.let { add(it) }
                    }
                }
            }
        }
    }

    val showableAircrafts = aircraftViewModel.getShowableAircrafts(filerViewModel.isFiltering, filerViewModel.filteredAirCrafts)
    val visibleAircrafts by remember (filerViewModel.isFiltering,filerViewModel.filteredAirCrafts.toList(), showableAircrafts, projection, screenWidth, screenHeight) {
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

    Canvas(modifier = Modifier
        .fillMaxSize()
        .clipToBounds()
        .pointerInput(type)
        {
            detectTransformGestures { _, pan, zoom, _ ->
                projection.pan(pan)
                projection.zoom(zoom)
            }
        })
    {
        visiblePoints.forEach { drawPoint(it) }
        visibleAircrafts.forEach { (_, position) ->
            drawAircraft(position)
        }
    }
}

fun DrawScope.drawPoint(coordinate: Coordinate)
{
    drawCircle(
        color = Color.Black,
        radius = 0.5f,
        center = Offset(coordinate.longitude!!.toFloat(), coordinate.latitude!!.toFloat())
    )
}