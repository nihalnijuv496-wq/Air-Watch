package org.airwatch.project.WorldMap

import org.airwatch.project.Aircraft.Coordinate
import org.airwatch.project.Utils.toRadians
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

data class ChunkCorners(
    val topLeftCorner: Coordinate,
    val bottomRightCorner: Coordinate
)
{
    //TODO{can i use !!. here}
    val minLat = bottomRightCorner.latitude ?:-90.0
    val maxLat = topLeftCorner.latitude ?: 90.0
    val minLon = topLeftCorner.longitude ?: -180.0
    val maxLon = bottomRightCorner.longitude ?: 180.0

    fun contains(coordinate: Coordinate): Boolean {
        val lat = coordinate.latitude ?: 91.0
        val lon = coordinate.longitude ?: 181.0
        return lat in minLat..maxLat && lon in minLon..maxLon
    }

    fun center() = Coordinate((minLat + maxLat) / 2, (minLon + maxLon) / 2)

    val isWholeSphere: Boolean = (maxLon - minLon) >= 359.999

    private fun toUnitVector(lat: Double, lon: Double): DoubleArray {
        val latRad = lat.toRadians()
        val lonRad = lon.toRadians()
        return doubleArrayOf(cos(latRad) * cos(lonRad), cos(latRad) * sin(lonRad), sin(latRad))
    }

    private val capCenterVec: DoubleArray by lazy {
        val c = center()
        toUnitVector(c.latitude ?: 0.0, c.longitude ?: 0.0)
    }

    val capAngularRadius: Double by lazy {
        if (isWholeSphere) PI
        else listOf(
            toUnitVector(maxLat, minLon), toUnitVector(maxLat, maxLon),
            toUnitVector(minLat, minLon), toUnitVector(minLat, maxLon)
        ).maxOf { v ->
            val dot = (capCenterVec[0]*v[0] + capCenterVec[1]*v[1] + capCenterVec[2]*v[2]).coerceIn(-1.0, 1.0)
            acos(dot)
        }
    }

    fun capDot(x: Double, y: Double, z: Double): Double =
        capCenterVec[0]*x + capCenterVec[1]*y + capCenterVec[2]*z
}


data class Chunk(val chunkCorners: ChunkCorners, val depth: Int)
{
    val coordinates = mutableListOf<Coordinate>()
    var children: Array<Chunk>? = null

    companion object
    {
        const val MAX_COORDINATES_PER_NODE = 15
        const val MAX_DEPTH = 10
        private const val LOD_PIXEL_THRESHOLD = 5.0
    }

    fun subdivide() {
        val (midLat, midLon) = chunkCorners.center()
        children = arrayOf(
            Chunk(ChunkCorners(topLeftCorner = chunkCorners.topLeftCorner , bottomRightCorner =Coordinate(midLat, midLon)), depth + 1), // NW
            Chunk(ChunkCorners(topLeftCorner = Coordinate(chunkCorners.topLeftCorner.latitude, midLon) , bottomRightCorner =Coordinate(midLat, chunkCorners.bottomRightCorner.longitude)), depth + 1), // NE
            Chunk(ChunkCorners(topLeftCorner = Coordinate(midLat, chunkCorners.topLeftCorner.longitude) , bottomRightCorner =Coordinate(chunkCorners.bottomRightCorner.latitude, midLon)), depth + 1), // SW
            Chunk(ChunkCorners(topLeftCorner = Coordinate(midLat, midLon) , bottomRightCorner = chunkCorners.bottomRightCorner), depth + 1)  // SE
        )
    }

    fun insert(coordinate: Coordinate)
    {
        if(!chunkCorners.contains(coordinate)) return

        if (children != null)
        {
            children!!.forEach { it.insert(coordinate) }
            return
        }

        if (depth >= MAX_DEPTH || coordinates.size < MAX_COORDINATES_PER_NODE)
        {
            coordinates.add(coordinate)
            return
        }

        subdivide()
        coordinates.add(coordinate)
        coordinates.forEach { c-> children!!.forEach { it.insert(c) }}
        coordinates.clear()
    }

    fun getDepth(root: Chunk?): Int{
       if(root!!.children == null)return 0
        val d0 = getDepth(root.children!![0])
        val d1 = getDepth(root.children!![1])
        val d2 = getDepth(root.children!![2])
        val d3 = getDepth(root.children!![3])

        return 1 + max(max(d0, d1), max(d2, d3))
    }

    fun traverse(
        projection: Projection,
        screenWidth: Int,
        screenHeight: Int,
        onRender: (Chunk) -> Unit
    ) {
        if (!projection.isChunkVisible(
                corners = chunkCorners,
                screenWidth = screenWidth,
                screenHeight = screenHeight
            )
        ) {
            return
        }

        val screenSpan = projection.chunkScreenSpan(chunkCorners)
        val currentChildren = children

        if (screenSpan < LOD_PIXEL_THRESHOLD || currentChildren == null) {
            onRender(this)
            return
        }

        for (child in currentChildren) {
            child.traverse(
                projection = projection,
                screenWidth = screenWidth,
                screenHeight = screenHeight,
                onRender = onRender
            )
        }
    }
}