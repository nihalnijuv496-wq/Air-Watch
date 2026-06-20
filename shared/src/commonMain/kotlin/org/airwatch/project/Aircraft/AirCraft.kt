package org.airwatch.project.Aircraft

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive


@Serializable
data class OpenSkyResponse(
    val time: Long,
    val states: List<JsonArray>
)

data class AirCraft (
    val icao24: String,
    val callSign: String?,
    val originCountry: String,
    val timePosition: Int?,
    val lastContact: Int?,
    val longitude: Double?,
    val latitude: Double?,
    val baroAltitude: Double?,
    val onGround: Boolean?,
    val velocity: Double?,
    val trueTrack: Double?,
    val verticalRate: Double?,
    //val sensors:  Array<Int>?,
    val geoAltitude: Double?,
    val squawk: String?,
    val spi: Boolean?,
)

fun JsonArray.toAirCraft(): AirCraft
{
    return AirCraft(
        icao24 = this[0].jsonPrimitive.content,
        callSign = this[1].jsonPrimitive.contentOrNull,
        originCountry = this[2].jsonPrimitive.content,
        timePosition = this[3].jsonPrimitive.intOrNull,
        lastContact = this[4].jsonPrimitive.intOrNull,
        longitude = this[5].jsonPrimitive.doubleOrNull,
        latitude = this[6].jsonPrimitive.doubleOrNull,
        baroAltitude = this[7].jsonPrimitive.doubleOrNull,
        onGround = this[8].jsonPrimitive.booleanOrNull,
        velocity = this[9].jsonPrimitive.doubleOrNull,
        trueTrack = this[10].jsonPrimitive.doubleOrNull,
        verticalRate = this[11].jsonPrimitive.doubleOrNull,
        //sensors = ,
        geoAltitude = this[13].jsonPrimitive.doubleOrNull,
        squawk = this[14].jsonPrimitive.contentOrNull,
        spi = this[15].jsonPrimitive.booleanOrNull,
    )
}