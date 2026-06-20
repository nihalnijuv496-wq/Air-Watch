package org.airwatch.project.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.airwatch.project.APICommunication.fetchFlights
import org.airwatch.project.Aircraft.AirCraft
import org.airwatch.project.UIComponents.RowDivider
import org.airwatch.project.UIComponents.ScrollableColumn
import org.airwatch.project.UIComponents.SideBar


@Composable
fun MenuScreen() {

    val logMessages = remember { mutableStateListOf<String>() } //TODO{"add time interval in which aircrafts are fetched"}
    var airCrafts by remember { mutableStateOf<List<AirCraft>>(emptyList()) }
    var isSideBarVisible by remember { mutableStateOf(false) }
    var isGlobeElseMap by remember { mutableStateOf(true)}


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
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
                    color = Color.White
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

        RowDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.65f)
        )
        {
            val scope = rememberCoroutineScope()
            Button(
                onClick = {scope.launch {
                    airCrafts = fetchFlights()
                    logMessages.add("fetched ${airCrafts.size} aircrafts")
                    airCrafts.forEach { println(it) }
                } },
                content = { Text (text = "getFlights")}
            )
        }

        RowDivider()

        ScrollableColumn(
            modifier = Modifier
                .fillMaxSize(),
            content = {
                logMessages.forEach {
                    Text(
                        text = it,
                        fontSize = 10.sp,
                        color = Color.White
                    )
                }
            },
            count = logMessages.size
        )

        SideBar(
            isVisible = isSideBarVisible,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = 0.65f)
                .background(color = Color.Red),
            content = {

            }
        )
    }
}