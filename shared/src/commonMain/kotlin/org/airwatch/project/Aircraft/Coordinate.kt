package org.airwatch.project.Aircraft

data class Coordinate(
    var latitude: Double?,
    var longitude: Double?

)

data class AreaByCoordinate(
    val startPosition: Coordinate,
    val endPosition: Coordinate,
)
{
    fun isEmpty(): Boolean =
        startPosition.latitude == null ||
        startPosition.longitude == null ||
        endPosition.latitude == null ||
        endPosition.longitude == null

    fun isInBound(position: Coordinate): Boolean {
        val isLongitudeInBound: Boolean
        val isLatitudeInBound: Boolean

        val startLongitude = startPosition.longitude ?: -180.0
        val endLongitude = endPosition.longitude ?: 180.0
        val startLatitude = startPosition.latitude ?:90.0
        val endLatitude = endPosition.latitude ?: -90.0
        val targetLongitude = position.longitude ?: 181.0
        val targetLatitude = position.latitude ?: 91.0

        isLongitudeInBound = if(startLongitude <= endLongitude &&
            targetLongitude in startLongitude..endLongitude) true
        else if(startLongitude > endLongitude &&
            targetLongitude !in endLongitude..startLongitude) true
        else false

        isLatitudeInBound = if(endLatitude <= startLatitude &&
            targetLatitude in endLatitude..startLatitude) true
        else if(endLatitude > startLatitude &&
            targetLatitude !in startLatitude..endLatitude) true
        else false

        return isLatitudeInBound && isLongitudeInBound
    }


    fun clear()
    {
        startPosition.latitude = null
        startPosition.longitude = null
        endPosition.latitude = null
        endPosition.longitude = null

    }
}