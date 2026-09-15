package org.airwatch.project.WorldMap.AircraftDetails

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import org.airwatch.project.UIComponents.RadarColors


fun DrawScope.drawAircrafts(points: Map<String, Offset>)
{
    points.forEach { (_, point) ->
        drawCircle(
            color = RadarColors.TextPrimary,
            radius = 2f,
            center = Offset(point.x, point.y)
        )
    }
}