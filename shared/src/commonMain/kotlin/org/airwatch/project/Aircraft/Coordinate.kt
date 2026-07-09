package org.airwatch.project.Aircraft

data class Coordinate(
    var latitude: Double?,
    var longitude: Double?
)

data class AreaRangeByCoordinate(
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

abstract class RangeByDouble()
{
    abstract var startValue: Double?
    abstract var endValue: Double?
    fun isEmpty(): Boolean =
        startValue == null || endValue == null

    fun isInBound(value: Double?): Boolean
    {
        val targetValue = value ?: -1.0
        val safeMinValue = startValue ?: 0.0
        val safeMaxValue = endValue ?: Double.MAX_VALUE

        return targetValue in safeMinValue..safeMaxValue
    }

    fun clear()
    {
        startValue = null
        endValue = null
    }
}
data class AltitudeRange(
    override var startValue: Double?,
    override var endValue: Double?
): RangeByDouble()
{
    var minAltitude: Double?
        get() = startValue
        set(value) { startValue = value }

    var maxAltitude: Double?
        get() = endValue
        set(value) { endValue = value }
}

data class VelocityRange(
    override var startValue: Double?,
    override var endValue: Double?
): RangeByDouble()
{
    var minVelocity: Double?
        get() = startValue
        set(value) { startValue = value }

    var maxVelocity: Double?
        get() = endValue
        set(value) { endValue = value }
}

data class AngleRange(
    override var startValue: Double?,
    override var endValue: Double?
): RangeByDouble()
{
    var minAngle: Double?
        get() = startValue
        set(value) { startValue = value }

    var maxAngle: Double?
        get() = endValue
        set(value) { endValue = value }

}
