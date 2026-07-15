package org.airwatch.project.WorldMap

import airwatch.shared.generated.resources.Res
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import org.airwatch.project.Aircraft.Coordinate
import org.jetbrains.compose.resources.ExperimentalResourceApi


@Serializable
data class WorldMap(
    val type: String,
    val countries: List<Country>
)

@Serializable
data class Country(
    val acro: String,
    val continent: String,
    val countryName: String,
    val geometry: Geometry
)
@Serializable
data class Geometry(
    val coordinates: JsonArray,
    val labelPos: JsonArray,
    val type: String
)

@OptIn(ExperimentalResourceApi::class)
suspend fun loadJsonFromResources(filePath: String): String {
    val bytes = Res.readBytes(filePath)
    return bytes.decodeToString()
}

suspend fun loadMapToTree(): Chunk
{
    val filePath = "files/WorldMap.json"
    val worldMapString = loadJsonFromResources(filePath)
    val worldMap: WorldMap = Json.decodeFromString(worldMapString)

    val tree = Chunk(
        ChunkCorners(
            Coordinate(90.0, -180.0),
            Coordinate(-90.0, 180.0)
        ),
        depth = 0
    )

    worldMap.countries.forEach { country ->
        country.geometry.coordinates.forEach { coord->
                getCoordinates(coord.jsonArray).collect{
                    tree.insert(it)
                }
        }
    }

    return tree
}

fun getCoordinates(coordinateArray: JsonArray): Flow<Coordinate> = flow()
{
    if (coordinateArray.isEmpty()) return@flow

    val firstElement = coordinateArray[0]

    if (firstElement is JsonPrimitive) emit(coordinateArray.toCoordinate())

    else if(firstElement is JsonArray)
    {
        coordinateArray.forEach {
            val currArray = it.jsonArray
            emitAll(getCoordinates(currArray))
        }
    }
}


fun JsonArray.toCoordinate(): Coordinate {
    return Coordinate(
        longitude = this[0].jsonPrimitive.doubleOrNull,
        latitude = this[1].jsonPrimitive.doubleOrNull
    )
}