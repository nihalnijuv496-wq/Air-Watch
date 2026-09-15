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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import org.airwatch.project.UIComponents.RadarColors
import org.airwatch.project.UIComponents.RowDivider

@Composable
fun FilterSideBarContent(data: List<AirCraft>, filterViewModel: FilterViewModel = viewModel()) {
    var currentFilterBar: String by remember { mutableStateOf("icao4") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = RadarColors.SurfaceRaised)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Filter", style = MaterialTheme.typography.headlineMedium)
        }

        ColumnDivider()

        Row(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.width(IntrinsicSize.Max).padding(8.dp)) {
                FilterTabButton("icao4", "ICAO4", currentFilterBar) { currentFilterBar = it }
                FilterTabButton("originCountry", "Origin country", currentFilterBar) { currentFilterBar = it }
                FilterTabButton("area", "Area", currentFilterBar) { currentFilterBar = it }
                FilterTabButton("altitude", "Altitude", currentFilterBar) { currentFilterBar = it }
                FilterTabButton("velocity", "Velocity", currentFilterBar) { currentFilterBar = it }
                FilterTabButton("direction", "Direction", currentFilterBar) { currentFilterBar = it }

                Spacer(modifier = Modifier.weight(1f))

                BasicButton(onClick = { filterViewModel.clearFilter() }) {
                    Text("Clear", style = MaterialTheme.typography.labelLarge)
                }
                Spacer(modifier = Modifier.padding(top = 4.dp))
                BasicButton(selected = true, onClick = { filterViewModel.filterAll(data) }) {
                    Text("Apply", style = MaterialTheme.typography.labelLarge)
                }
            }

            RowDivider()

            Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                val queries = FilterViewModel.Queries
                when (currentFilterBar) {
                    "icao4" -> ICAO4Bar(data.mapTo(HashSet()) { it.icao24 }, queries.icao4Queries)
                    "originCountry" -> CountryBar(data.mapTo(HashSet()) { it.originCountry }, queries.countryQueries)
                    "area" -> AreaBar(queries.areaQuery)
                    "altitude" -> AltitudeBar(queries.altitudeQuery)
                    "velocity" -> VelocityBar(queries.velocityQuery)
                    "direction" -> DirectionBar(queries.angleQuery)
                    else -> currentFilterBar = "icao4"
                }
            }
        }
    }
}


@Composable
private fun FilterTabButton(id: String, label: String, currentFilterBar: String, onSelect: (String) -> Unit) {
    BasicButton(selected = currentFilterBar == id, onClick = { onSelect(id) }) {
        Text(label, style = MaterialTheme.typography.bodySmall)

    }
    Spacer(modifier = Modifier.padding(top = 2.dp))
}