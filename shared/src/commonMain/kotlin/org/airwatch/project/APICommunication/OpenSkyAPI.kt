package org.airwatch.project.APICommunication

import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

suspend fun fetchFlights():String
{
    val response: HttpResponse = httpClient.get("https://opensky-network.org/api/states/all")
    {
        parameter("lamin", 8.0)
        parameter("lamax", 13.0)
        parameter("lomin", 74.0)
        parameter("lomax", 80.0)
    }

    return response.bodyAsText()
}