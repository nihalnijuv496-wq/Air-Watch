package org.airwatch.project.WorldMap.AircraftDrawer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope


fun DrawScope.drawAircrafts(points: Map<String, Offset>)
{
    points.forEach { (_, point) ->
        drawCircle(
            color = Color.Blue,
            radius = 2f,
            center = Offset(point.x, point.y)
        )
    }
}