package org.airwatch.project.WorldMap.AircraftDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.airwatch.project.Aircraft.AirCraft


@Composable
fun AircraftDetailCard(
    aircraft: AirCraft,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = onDismiss
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = aircraft.icao24,
                style = MaterialTheme.typography.titleMedium
            )
            val texts = listOf(
                "Lat: ${aircraft.position.latitude}, Lon: ${aircraft.position.longitude}",
                "Origin Country: ${aircraft.originCountry}",
                "Baro Altitude: ${aircraft.baroAltitude} m",
                "Geo Altitude: ${aircraft.geoAltitude} m",
                "Velocity: ${aircraft.velocity} m/s",
                "Track Direction: ${aircraft.trueTrack}°",
                "Vertical Rate: ${aircraft.verticalRate} m/s"
            )
            for (text in texts)
            {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
