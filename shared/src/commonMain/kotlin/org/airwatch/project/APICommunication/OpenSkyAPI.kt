package org.airwatch.project.APICommunication

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.airwatch.project.Aircraft.AirCraft
import org.airwatch.project.Aircraft.OpenSkyResponse
import org.airwatch.project.Aircraft.toAirCraft
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant


@Serializable
data class TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_in") val expiresIn: Long // seconds
)

class OpenSkyAuth(
    private val httpClient: HttpClient,
    private val clientId: String,
    private val clientSecret: String
) {
    private var cachedToken: String? = null
    private var tokenExpiresAt: Instant = Instant.DISTANT_PAST

    suspend fun getToken(): String {


        if (cachedToken != null && Clock.System.now() < tokenExpiresAt) {
            return cachedToken!!
        }

        val response: TokenResponse = httpClient.submitForm(
            url = "https://auth.opensky-network.org/auth/realms/opensky-network/protocol/openid-connect/token",
            formParameters = Parameters.build {
                append("grant_type", "client_credentials")
                append("client_id", clientId)
                append("client_secret", clientSecret)
            }
        ).body()

        cachedToken = response.accessToken
        tokenExpiresAt = Clock.System.now() + (response.expiresIn - 30).seconds

        return response.accessToken

        /*if (cachedToken != null && Clock.System.now() < tokenExpiresAt) {
            return cachedToken!!
        }

        val httpResponse = httpClient.submitForm(
            url = "https://auth.opensky-network.org/auth/realms/opensky-network/protocol/openid-connect/token",
            formParameters = Parameters.build {
                append("grant_type", "client_credentials")
                append("client_id", clientId)
                append("client_secret", clientSecret)
            }
        )

        val rawBody = httpResponse.bodyAsText()
        val response: TokenResponse = Json { ignoreUnknownKeys = true }.decodeFromString(rawBody)

        cachedToken = response.accessToken
        tokenExpiresAt = Clock.System.now() + (response.expiresIn - 30).seconds

        return response.accessToken*/
    }
}

class FlightTracker(
    private val httpClient: HttpClient,
    private val auth: OpenSkyAuth,
    private val creditsPerCall: Int = 4
) {
    private var remainingCredits: Int? = null

    suspend fun fetchFlights(): List<AirCraft> {
        val token = auth.getToken()

        val httpResponse = httpClient.get("https://opensky-network.org/api/states/all") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }

        remainingCredits = httpResponse.headers["X-Rate-Limit-Remaining"]?.toIntOrNull()

        if (httpResponse.status == HttpStatusCode.TooManyRequests) {
            val retryAfter = httpResponse.headers["X-Rate-Limit-Retry-After-Seconds"]?.toLongOrNull() ?: 60L
            throw RateLimitExceededException(retryAfter)
        }

        val response: OpenSkyResponse = httpResponse.body()
        return response.states.map { it.toAirCraft() }
    }


    fun flightUpdates(): Flow<List<AirCraft>> = flow {
        while (currentCoroutineContext().isActive) {
            try {
                val flights = fetchFlights()
                emit(flights)
                delay(nextPollDelayMillis())
            } catch (e: RateLimitExceededException) {
                delay(e.retryAfterSeconds * 1000)
            }
        }
    }

    private fun nextPollDelayMillis(): Long {
        val remaining = remainingCredits ?: return 30_000L

        if (remaining <= 0) return secondsUntilMidnightUTC() * 1000

        val callsLeft = (remaining / creditsPerCall).coerceAtLeast(1)
        val secondsLeft = secondsUntilMidnightUTC().coerceAtLeast(1)
        val intervalSeconds = (secondsLeft / callsLeft).coerceAtLeast(5)

        return intervalSeconds * 1000
    }

    fun secondsUntilMidnightUTC(): Long {
        val currentSecond = Clock.System.now().epochSeconds % 86400
        return (86400 - currentSecond)
    }
}

class RateLimitExceededException(val retryAfterSeconds: Long) : Exception()