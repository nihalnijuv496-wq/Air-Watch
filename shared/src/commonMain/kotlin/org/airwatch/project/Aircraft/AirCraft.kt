package org.airwatch.project.Aircraft

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
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
    val position: Coordinate,
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
/*
var _airCrafts = mutableStateOf<List<AirCraft>>(emptyList())
val airCrafts: List<AirCraft> get() = _airCrafts.value

fun updateAircraftList(newList:MutableState<List<AirCraft>>)
{
    _airCrafts = newList
}

fun currShowableAirCrafts() = if(Filter.isFiltering) Filter.filteredAirCrafts else airCrafts
//TODO{"change the data structure to a map to fasten the filtering. also add a viewmodel for this"}
*/

class AircraftViewModel : ViewModel() {

    private val _airCrafts = mutableStateOf<List<AirCraft>>(emptyList())

    val airCrafts: State<List<AirCraft>> get() = _airCrafts

    fun updateAircraftList(newList: List<AirCraft>) {
        _airCrafts.value = newList
    }

    fun getShowableAircrafts(isFiltering: Boolean, filteredAirCrafts: List<AirCraft>) =
        if (isFiltering) filteredAirCrafts else _airCrafts.value
}



fun JsonArray.toAirCraft(): AirCraft
{
    return AirCraft(
        icao24 = this[0].jsonPrimitive.content,
        callSign = this[1].jsonPrimitive.contentOrNull,
        originCountry = this[2].jsonPrimitive.content,
        timePosition = this[3].jsonPrimitive.intOrNull,
        lastContact = this[4].jsonPrimitive.intOrNull,
        position = Coordinate(
            longitude = this[5].jsonPrimitive.doubleOrNull,
            latitude = this[6].jsonPrimitive.doubleOrNull),
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