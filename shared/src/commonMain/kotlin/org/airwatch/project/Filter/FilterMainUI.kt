package org.airwatch.project.Filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.airwatch.project.Aircraft.AirCraft
import org.airwatch.project.Filter.FilterSubUI.AltitudeBar
import org.airwatch.project.Filter.FilterSubUI.AreaBar
import org.airwatch.project.Filter.FilterSubUI.CountryBar
import org.airwatch.project.Filter.FilterSubUI.DirectionBar
import org.airwatch.project.Filter.FilterSubUI.ICAO4Bar
import org.airwatch.project.Filter.FilterSubUI.VelocityBar
import org.airwatch.project.UIComponents.BasicButton
import org.airwatch.project.UIComponents.ColumnDivider
import org.airwatch.project.UIComponents.RowDivider
import org.airwatch.project.UIComponents.secondaryColor
import org.airwatch.project.UIComponents.textColor

@Composable
fun FilterSideBarContent(data: List<AirCraft>, filterViewModel: FilterViewModel = viewModel())
{
    var currentFilterBar: String by remember { mutableStateOf("icao4") }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = secondaryColor))
    {
        Row(
            modifier = Modifier
                .padding((10).dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        )
        {
            Text(
                text = "Filter",
                fontSize = 20.sp,
                color = textColor
            )
        }

        ColumnDivider()


        Row(modifier = Modifier
            .fillMaxSize())
        {
            Column(modifier = Modifier.width(IntrinsicSize.Max))
            {
                BasicButton(content = { Text("icao4") }, onClick = {currentFilterBar = "icao4"})
                BasicButton(content = { Text("OriginCountry") }, onClick = {currentFilterBar = "originCountry"})
                BasicButton(content = { Text("Area") }, onClick = {currentFilterBar = "area"})
                BasicButton(content = { Text("Altitude") }, onClick = {currentFilterBar = "altitude"})
                BasicButton(content = { Text("Velocity") }, onClick = {currentFilterBar = "velocity"})
                BasicButton(content = { Text("Direction") }, onClick = {currentFilterBar = "direction"})

                Spacer(modifier = Modifier.weight(1f))

                BasicButton(
                    onClick = { filterViewModel.clearFilter() },
                    content = { Text("Clear") }
                )
                BasicButton(content = { Text("Apply") }, onClick = {
                    filterViewModel.filterAll(data)
                })
            }

            RowDivider()

            Column(modifier = Modifier.fillMaxSize())
            {
                val queries = FilterViewModel.Queries
                when(currentFilterBar)
                {
                    "icao4" -> {
                        ICAO4Bar(data.mapTo(HashSet()){it.icao24}, queries.icao4Queries)
                    }

                    "originCountry" -> {
                        CountryBar(data.mapTo(HashSet()) { it.originCountry }, queries.countryQueries)
                    }

                    "area" -> {
                        AreaBar(queries.areaQuery)
                    }

                    "altitude" -> {
                        AltitudeBar(queries.altitudeQuery)
                    }

                    "velocity" -> {
                        VelocityBar(queries.velocityQuery)
                    }

                    "direction" -> {
                        DirectionBar(queries.angleQuery)
                    }

                    else -> {
                        currentFilterBar = "icao4"
                    }


                }
            }

        }

    }
}

