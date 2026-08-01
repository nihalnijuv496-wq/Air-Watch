package org.airwatch.project.WorldMap

import org.airwatch.project.Aircraft.Coordinate
import org.airwatch.project.Utils.toRadians
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

interface Projection {
    fun project(coordinate: Coordinate): Coordinate?
    fun isChunkVisible(
        corners: ChunkCorners,
        screenWidth: Int,
        screenHeight: Int,
    ): Boolean
    fun chunkScreenSpan(corners: ChunkCorners): Double
}

class EquirectangularProjection(
    val viewportWidth: Double,
    val viewportHeight: Double,
    val centerLat: Double = 0.0,
    val centerLon: Double = 0.0,
    val zoom: Float = 4.5f
) : Projection {

    override fun project(coordinate: Coordinate): Coordinate {

        var deltaLon = coordinate.longitude!! - centerLon
        if (deltaLon > 180) deltaLon -= 360
        if (deltaLon < -180) deltaLon += 360

        val deltaLat = coordinate.latitude!! - centerLat

        val x = viewportWidth / 2f + (deltaLon * zoom)
        val y = viewportHeight / 2f - (deltaLat * zoom)

        return Coordinate(y, x) //always non null, in pixels
    }

    override fun isChunkVisible(
        corners: ChunkCorners,
        screenWidth: Int,
        screenHeight: Int,
    ): Boolean
    {
        val topLeftCorner = project(corners.topLeftCorner)
        val bottomRightCorner = project(corners.bottomRightCorner)

        if (
            topLeftCorner.longitude!! < screenWidth &&
            bottomRightCorner.longitude!! > 0 &&
            topLeftCorner.latitude!! < screenHeight &&
            bottomRightCorner.latitude!! > 0
        ) return true

        return false
    }

    override fun chunkScreenSpan(corners: ChunkCorners): Double
    {
        val topLeftCorner = project(corners.topLeftCorner)
        val bottomRightCorner = project(corners.bottomRightCorner)

        return max(
            abs(topLeftCorner.latitude!! - bottomRightCorner.latitude!!),
            abs(topLeftCorner.longitude!! - bottomRightCorner.longitude!!)
        )
    }

}

class OrthographicProjection(
    val cameraLat: Double = 0.0,
    val cameraLon: Double = 0.0,
    val viewportWidth: Double,
    val viewportHeight: Double,
    val globeRadiusPx: Double = 350.0
) : Projection {

    override fun project(coordinate: Coordinate): Coordinate? {
        val latRad = coordinate.latitude!!.toRadians()
        val lonRad = coordinate.longitude!!.toRadians()
        val camLatRad = cameraLat.toRadians()
        val camLonRad = cameraLon.toRadians()

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
        val screenX = viewportWidth / 2f + (y2 * globeRadiusPx)
        val screenY = viewportHeight / 2f - (z2 * globeRadiusPx)

        return Coordinate(screenY, screenX) // value is in pixels
    }

    override fun chunkScreenSpan(corners: ChunkCorners): Double {
        val projected = projectedCorners(corners)

        if (projected.size < 2) return 0.0

        val minX = projected.minOf { it.longitude!! }
        val maxX = projected.maxOf { it.longitude!! }
        val minY = projected.minOf { it.latitude!! }
        val maxY = projected.maxOf { it.latitude!! }

        return max(maxX - minX, maxY - minY)
    }

    override fun isChunkVisible(
        corners: ChunkCorners,
        screenWidth: Int,
        screenHeight: Int,
    ): Boolean {
        val projected = projectedCorners(corners)

        if (projected.isEmpty()) return false

        val minX = projected.minOf { it.longitude!! }
        val maxX = projected.maxOf { it.longitude!! }
        val minY = projected.minOf { it.latitude!! }
        val maxY = projected.maxOf { it.latitude!! }

        return minX < screenWidth && maxX > 0 && minY < screenHeight && maxY > 0
    }

    private fun projectedCorners(corners: ChunkCorners): List<Coordinate> {
        val topLat = corners.topLeftCorner.latitude!!
        val bottomLat = corners.bottomRightCorner.latitude!!
        val leftLon = corners.topLeftCorner.longitude!!
        val rightLon = corners.bottomRightCorner.longitude!!
        val midLat = (topLat + bottomLat) / 2
        val midLon = (leftLon + rightLon) / 2

        val samplePoints = listOf(
            Coordinate(topLat, leftLon),
            Coordinate(topLat, rightLon),
            Coordinate(bottomLat, leftLon),
            Coordinate(bottomLat, rightLon),
            Coordinate(topLat, midLon),
            Coordinate(bottomLat, midLon),
            Coordinate(midLat, leftLon),
            Coordinate(midLat, rightLon),
        )

        return samplePoints.mapNotNull { project(it) }
    }


}

