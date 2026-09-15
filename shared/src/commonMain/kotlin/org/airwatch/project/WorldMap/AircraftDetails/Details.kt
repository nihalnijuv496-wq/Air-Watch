package org.airwatch.project.WorldMap.AircraftDetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.airwatch.project.Aircraft.AirCraft
import org.airwatch.project.UIComponents.RadarColors


private fun Double.formatFixed(decimals: Int): String {
    val multiplier = generateSequence(1.0) { it * 10 }.take(decimals + 1).last()
    val rounded = kotlin.math.round(this * multiplier) / multiplier
    val parts = rounded.toString().split(".")
    val intPart = parts[0]
    if (decimals == 0) return intPart
    val fracPart = (parts.getOrElse(1) { "" }).padEnd(decimals, '0').take(decimals)
    return "$intPart.$fracPart"
}
@Composable
fun AircraftDetailCard(
    aircraft: AirCraft,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = onDismiss,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = RadarColors.Surface),
        border = BorderStroke(1.dp, RadarColors.Border),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // flat HUD panel, not a drop shadow
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = aircraft.icao24,
                style = MaterialTheme.typography.headlineMedium
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = RadarColors.Border
            )

            val readouts = listOf(
                "Lat" to "${aircraft.position.latitude?.formatFixed(4)}°",
                "Lon" to "${aircraft.position.longitude?.formatFixed(4)}°",
                "Origin" to aircraft.originCountry,
                "Baro alt" to "${aircraft.baroAltitude} m",
                "Geo alt" to "${aircraft.geoAltitude} m",
                "Velocity" to "${aircraft.velocity} m/s",
                "Track" to "${aircraft.trueTrack}°",
                "Vert rate" to "${aircraft.verticalRate} m/s"
            )

            for ((label, value) in readouts) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(22.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = RadarColors.TextMuted
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodySmall, // monospace — aligns the digits
                        color = RadarColors.Amber
                    )
                }
            }
        }
    }
}