package org.airwatch.project.WorldMap

import androidx.compose.ui.geometry.Offset
import org.airwatch.project.Aircraft.Coordinate
import org.airwatch.project.Utils.toRadians
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

interface Projection {
    val screenOffsets: ProjectionOffset
    fun project(coordinate: Coordinate): Coordinate?
    fun isChunkVisible(
        corners: ChunkCorners,
        screenWidth: Int,
        screenHeight: Int,
    ): Boolean
    fun chunkScreenSpan(corners: ChunkCorners): Double

    fun pan(offset: Offset)
    fun zoom(factor: Float)
}

class EquirectangularProjection(
    val viewportWidth: Double,
    val viewportHeight: Double
) : Projection {

    override val screenOffsets = EquirectangularProjectionOffset()

    override fun project(coordinate: Coordinate): Coordinate {

        var deltaLon = coordinate.longitude!! - screenOffsets.cameraLon
        deltaLon = ((deltaLon + 180.0).mod(360.0)) - 180.0

        val deltaLat = coordinate.latitude!! - screenOffsets.cameraLat

        val x = viewportWidth / 2f + (deltaLon * screenOffsets.scale)
        val y = viewportHeight / 2f - (deltaLat * screenOffsets.scale)

        return Coordinate(y, x) //always non null, in pixels
    }

    override fun isChunkVisible(
        corners: ChunkCorners,
        screenWidth: Int,
        screenHeight: Int,
    ): Boolean
    {
        val halfSpan = (corners.maxLon - corners.minLon) / 2.0
        var centerDelta = ((corners.minLon + corners.maxLon) / 2.0) - screenOffsets.cameraLon
        centerDelta = ((centerDelta + 180.0).mod(360.0)) - 180.0

        val minDelta = centerDelta - halfSpan
        val maxDelta = centerDelta + halfSpan

        val minX = viewportWidth/2f + (minDelta*screenOffsets.scale)
        val maxX = viewportWidth/2f + (maxDelta*screenOffsets.scale)

        val topY = viewportHeight/2f - ((corners.maxLat - screenOffsets.cameraLat)*screenOffsets.scale)
        val bottomY = viewportHeight/2f - ((corners.minLat - screenOffsets.cameraLat)*screenOffsets.scale)

        return minX < screenWidth && maxX > 0 && topY < screenHeight && bottomY > 0
    }

    override fun chunkScreenSpan(corners: ChunkCorners): Double
    {
        val latSpanPx = (corners.maxLat - corners.minLat) * screenOffsets.scale
        val lonSpanPx = (corners.maxLon - corners.minLon) * screenOffsets.scale

        return max(latSpanPx, lonSpanPx)
    }

    fun clampCameraLat() {
        val halfHeightDeg = (viewportHeight / 2.0) / screenOffsets.scale
        val minLat = -90.0 + halfHeightDeg
        val maxLat = 90.0 - halfHeightDeg

        screenOffsets.cameraLat = if (minLat > maxLat) {
            0.0
        } else {
            screenOffsets.cameraLat.coerceIn(minLat, maxLat)
        }
    }

    override fun pan(offset: Offset) {
        screenOffsets.updateCameraPos(offset)
        clampCameraLat()
    }

    override fun zoom(factor: Float) {
        screenOffsets.changeScale(factor)
        clampCameraLat()
    }
}

class OrthographicProjection(
    val viewportWidth: Double,
    val viewportHeight: Double,
) : Projection {

    override val screenOffsets = OrthographicProjectionOffset()

    override fun project(coordinate: Coordinate): Coordinate? {
        val latRad = coordinate.latitude!!.toRadians()
        val lonRad = coordinate.longitude!!.toRadians()
        val camLatRad = screenOffsets.cameraLat.toRadians()
        val camLonRad = screenOffsets.cameraLon.toRadians()

        // converting to 3d unit sphere coordinate
        val x0 = cos(latRad) * cos(lonRad)
        val y0 = cos(latRad) * sin(lonRad)
        val z0 = sin(latRad)

        // rotate around Z axis by -cameraLon to bring camera's longitude to front
        val x1 = x0 * cos(camLonRad) + y0 * sin(camLonRad)
        val y1 = -x0 * sin(camLonRad) + y0 * cos(camLonRad)
        val z1 = z0

        // rotate around Y axis by -cameraLat to bring camera's latitude to front
        val x2 = x1 * cos(camLatRad) + z1 * sin(camLatRad)
        val y2 = y1
        val z2 = -x1 * sin(camLatRad) + z1 * cos(camLatRad)

        // after both rotation: we got the position of the coordinate with respect to camera position
        // if x2 negative, it's on the back side of the sphere
        if (x2 < 0) return null

        // orthographic drop: just take y2, z2 as screen coordinates, ignore x2
        val screenX = viewportWidth / 2f + (y2 * screenOffsets.globeRadiusPx)
        val screenY = viewportHeight / 2f - (z2 * screenOffsets.globeRadiusPx)

        return Coordinate(screenY, screenX) // value is in pixels
    }

    override fun chunkScreenSpan(corners: ChunkCorners): Double =
        if (corners.isWholeSphere) return Double.MAX_VALUE
        else 2.0 * sin(corners.capAngularRadius) * screenOffsets.globeRadiusPx

    override fun isChunkVisible(
        corners: ChunkCorners,
        screenWidth: Int,
        screenHeight: Int,
    ): Boolean {
        if (corners.isWholeSphere) return true

        val camLatRad = screenOffsets.cameraLat.toRadians()
        val camLonRad = screenOffsets.cameraLon.toRadians()
        val camX = cos(camLatRad) * cos(camLonRad)
        val camY = cos(camLatRad) * sin(camLonRad)
        val camZ = sin(camLatRad)

        // visible iff the chunk's cap overlaps the camera-facing hemisphere:
        // angle(camera, chunkCenter) < 90° + capAngularRadius  <=>  dot > -sin(capAngularRadius)
        return corners.capDot(camX, camY, camZ) > -sin(corners.capAngularRadius)
    }

    override fun pan(offset: Offset) {
        screenOffsets.updateCameraPos(offset)
    }

    override fun zoom(factor: Float) {
        screenOffsets.changeScale(factor)
    }
}

