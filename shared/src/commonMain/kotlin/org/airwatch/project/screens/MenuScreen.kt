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
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.airwatch.project.APICommunication.fetchFlights
import org.airwatch.project.Aircraft.airCrafts
import org.airwatch.project.Aircraft.currShowableAirCrafts
import org.airwatch.project.Aircraft.updateAircraftList
import org.airwatch.project.Filter.FilterSideBarContent
import org.airwatch.project.UIComponents.ColumnDivider
import org.airwatch.project.UIComponents.ScrollableColumn
import org.airwatch.project.UIComponents.SideBar
import org.airwatch.project.UIComponents.backGroundColor
import org.airwatch.project.UIComponents.textColor


@Composable
fun MenuScreen() {

    val logMessages = remember { mutableStateListOf<String>() } //TODO{"add time interval in which aircrafts are fetched"}
    var isSideBarVisible by remember { mutableStateOf(false) }
    var isGlobeElseMap by remember { mutableStateOf(true)}


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
                        onClick = {isGlobeElseMap = !isGlobeElseMap},
                        content = {Text( if(isGlobeElseMap) "toMap" else "toGlobe" )}
                    )
                }
            }

            ColumnDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.65f)
            )
            {
                val scope = rememberCoroutineScope()
                Button(
                    onClick = {scope.launch {
                        updateAircraftList(fetchFlights())
                        logMessages.add("fetched ${currShowableAirCrafts().size} aircrafts")
                        currShowableAirCrafts().forEach { println(it) }
                    } },
                    content = { Text (text = "getFlights")}
                )
                Button(
                    onClick = {currShowableAirCrafts().forEach {
                        println("* $it")
                    }},
                    content = {Text("showVisible")})
                Button(
                    onClick = {airCrafts.forEach {
                        println("* $it")
                    }},
                    content = {Text("showFull")})
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
            contentFun = { FilterSideBarContent(data = airCrafts) }
        )
    }

}