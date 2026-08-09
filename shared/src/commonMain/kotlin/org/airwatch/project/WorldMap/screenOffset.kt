package org.airwatch.project.WorldMap

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset

interface ProjectionOffset {

    var cameraLat: Double
    var cameraLon: Double
    fun updateCameraPos(offset: Offset)
    fun changeScale(zoom: Float)
}

class OrthographicProjectionOffset : ProjectionOffset
{
    override var cameraLat by mutableStateOf(0.0)
    override var cameraLon by mutableStateOf(0.0)

    val MIN_RADIUS = 250.0
    val MAX_RADIUS = 1000.0
    var globeRadiusPx: Double = MIN_RADIUS

    override fun updateCameraPos(offset: Offset)
    {
        cameraLon -= 0.75*offset.x*MIN_RADIUS/globeRadiusPx
        cameraLat += 0.75*offset.y*MIN_RADIUS/globeRadiusPx
    }

    override fun changeScale(zoom: Float) {
        globeRadiusPx = (globeRadiusPx*zoom).coerceIn(MIN_RADIUS, MAX_RADIUS)
    }

}

class EquirectangularProjectionOffset : ProjectionOffset
{
    override var cameraLat by mutableStateOf(0.0)
    override var cameraLon by mutableStateOf(0.0)
    val MIN_SCALE = 4.5
    val MAX_SCALE = 10.0
    var scale = MIN_SCALE


    override fun updateCameraPos(offset: Offset) {
        cameraLon = (((cameraLon - offset.x*MIN_SCALE/scale) + 180.0).mod(360.0)) - 180.0
        cameraLat += offset.y * MIN_SCALE / scale
    }

    override fun changeScale(zoom: Float) {
        scale = (scale*zoom).coerceIn(MIN_SCALE, MAX_SCALE)
    }

}