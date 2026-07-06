package org.airwatch.project.Filter

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import org.airwatch.project.Aircraft.AirCraft
import org.airwatch.project.Aircraft.AreaRangeByCoordinate
import org.airwatch.project.Aircraft.Coordinate
import org.airwatch.project.Aircraft.PositionRangeByAltitude
import org.airwatch.project.Aircraft.airCrafts
import org.airwatch.project.Aircraft.currShowableAirCrafts
import org.airwatch.project.Filter.Filter.Queries.altitudeQuery
import org.airwatch.project.Filter.Filter.Queries.areaQuery
import org.airwatch.project.Filter.Filter.Queries.clearQueries
import org.airwatch.project.Filter.Filter.Queries.countryQueries
import org.airwatch.project.Filter.Filter.Queries.icao4Queries


object Filter
{
    private val pFilteredAirCrafts = mutableStateListOf<AirCraft>()
    val filteredAirCrafts: MutableList<AirCraft> get() = pFilteredAirCrafts
    var isFiltering = false



    object Queries
    {
        val icao4Queries = mutableStateListOf<String>()
        val countryQueries = mutableStateListOf<String>()
        val areaQuery = mutableStateOf(
            AreaRangeByCoordinate(
                startPosition = Coordinate(null, null),
                endPosition = Coordinate(null, null)
            )
        ).value
        val altitudeQuery = mutableStateOf(
            PositionRangeByAltitude(minAltitude = null, maxAltitude = null)
        ).value

        fun clearQueries()
        {
            icao4Queries.clear()
            countryQueries.clear()
            areaQuery.clear()
        }
    }


    fun clearFilter()
    {
        isFiltering = false
        clearQueries()
    }

    fun setFilteredAirCraft(newList: List<AirCraft>)
    {
        filteredAirCrafts.clear()
        newList.forEach { filteredAirCrafts.add(it) }
    }

    fun filterAll() {
        setFilteredAirCraft(
            airCrafts.filter{
                (icao4Queries.isEmpty() || it.icao24 in icao4Queries) &&
                        (countryQueries.isEmpty() || it.originCountry in countryQueries) &&
                        (areaQuery.isEmpty() || areaQuery.isInBound(it.position)) &&
                        (altitudeQuery.isEmpty() || altitudeQuery.isInBound(it.baroAltitude))
            }
        )
        isFiltering = true
        println("---")
        currShowableAirCrafts().forEach { println("*${it}") }
        println(countryQueries)
    }

}





