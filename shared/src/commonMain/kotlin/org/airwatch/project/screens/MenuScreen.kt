package org.airwatch.project.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.airwatch.project.APICommunication.fetchFlights
import org.airwatch.project.Aircraft.AircraftViewModel
import org.airwatch.project.Filter.FilterSideBarContent
import org.airwatch.project.Filter.FilterViewModel
import org.airwatch.project.UIComponents.ColumnDivider
import org.airwatch.project.UIComponents.ScrollableColumn
import org.airwatch.project.UIComponents.SideBar
import org.airwatch.project.UIComponents.backGroundColor
import org.airwatch.project.UIComponents.textColor
import org.airwatch.project.WorldMap.DrawMapCanvas


@Composable
fun MenuScreen(
    aircraftViewModel: AircraftViewModel = viewModel(),
    filerViewModel: FilterViewModel = viewModel ()) {


    val logMessages = remember { mutableStateListOf<String>() } //TODO{"add time interval in which aircrafts are fetched", "abstract the log screen to new file"}
    var isSideBarVisible by remember { mutableStateOf(false) }
    var mapState by remember { mutableStateOf("Plane") }
    var mapScreenSize by remember { mutableStateOf(Pair(0, 0)) }


    Box(modifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.statusBars)
        .windowInsetsPadding(WindowInsets.navigationBars)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = backGroundColor)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ){isSideBarVisible = false},

            )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.17f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(fraction = 0.5f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Name",
                        fontSize = 25.sp,
                        color = textColor
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(fraction = 0.5f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {isSideBarVisible = true},
                        content = { Text (text = "filter")}
                    )


                    Button(
                        onClick = {
                                mapState = if(mapState == "Plane") "Globe" else "Plane"
                            },
                        content = {Text( if(mapState == "Plane") "toGlobe" else "toPlane" )}
                    )
                }
                val scope = rememberCoroutineScope()
                Button(
                    onClick = {scope.launch {
                        aircraftViewModel.updateAircraftList(fetchFlights())
                        logMessages.add("fetched ${aircraftViewModel.getShowableAircrafts(filerViewModel.isFiltering, filerViewModel.filteredAirCrafts).size} aircrafts")
                        aircraftViewModel.getShowableAircrafts(filerViewModel.isFiltering, filerViewModel.filteredAirCrafts).forEach { println(it) }
                    } },
                    content = { Text (text = "getFlights")}
                )
                Button(
                    onClick = {aircraftViewModel.getShowableAircrafts(filerViewModel.isFiltering, filerViewModel.filteredAirCrafts).forEach {
                        println("* $it")
                    }},
                    content = {Text("showVisible")})
                Button(
                    onClick = {aircraftViewModel.airCrafts.value.forEach {
                        println("* $it")
                    }},
                    content = {Text("showFull")})
            }

            ColumnDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.65f)
                    .onGloballyPositioned{ coordinates ->
                        mapScreenSize = Pair(coordinates.size.height, coordinates.size.width)
                    }
            )
            {


                DrawMapCanvas(type = mapState, screenHeight = mapScreenSize.first, screenWidth = mapScreenSize.second)


            }

            ColumnDivider()

            ScrollableColumn(
                modifier = Modifier
                    .fillMaxSize(),
                content = {
                    logMessages.forEach {
                        Text(
                            text = it,
                            fontSize = 10.sp,
                            color = textColor
                        )
                    }
                },
                count = logMessages.size
            )
        }

        SideBar(
            isVisible = isSideBarVisible,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.85f)
                .align(Alignment.CenterStart),
            contentFun = { FilterSideBarContent(data = aircraftViewModel.airCrafts.value) }
        )
    }

}