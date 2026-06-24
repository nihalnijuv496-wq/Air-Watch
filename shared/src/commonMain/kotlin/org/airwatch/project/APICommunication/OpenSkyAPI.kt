package org.airwatch.project.APICommunication

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.airwatch.project.Aircraft.AirCraft
import org.airwatch.project.Aircraft.OpenSkyResponse
import org.airwatch.project.Aircraft.toAirCraft

suspend fun fetchFlights(): MutableState<List<AirCraft>>
{
    val response: OpenSkyResponse = httpClient.get("https://opensky-network.org/api/states/all")
    {
        parameter("lamin", 8.0)
        parameter("lamax", 13.0)
        parameter("lomin", 74.0)
        parameter("lomax", 80.0)
    }.body()

    return mutableStateOf(response.states.map { it.toAirCraft() })
}