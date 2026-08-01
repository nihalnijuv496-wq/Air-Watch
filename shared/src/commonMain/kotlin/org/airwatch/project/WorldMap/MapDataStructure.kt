package org.airwatch.project.WorldMap

import org.airwatch.project.Aircraft.Coordinate
import kotlin.math.max

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

}


data class Chunk(val chunkCorners: ChunkCorners, val depth: Int)
{
    val coordinates = mutableListOf<Coordinate>()
    var children: Array<Chunk>? = null

    companion object
    {
        const val MAX_COORDINATES_PER_NODE = 15
        const val MAX_DEPTH = 10
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
}