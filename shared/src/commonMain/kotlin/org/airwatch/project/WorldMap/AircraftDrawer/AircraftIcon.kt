package org.airwatch.project.WorldMap.AircraftDrawer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import org.airwatch.project.Aircraft.Coordinate



fun DrawScope.drawAircraft(coordinate: Coordinate)
{
    drawCircle(
        color = Color.Blue,
        radius = 2f,
        center = Offset(coordinate.longitude!!.toFloat(), coordinate.latitude!!.toFloat())
    )
}