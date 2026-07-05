package org.airwatch.project.Filter

import androidx.compose.runtime.mutableStateListOf
import org.airwatch.project.Aircraft.AirCraft
import org.airwatch.project.Aircraft.AreaByCoordinate
import org.airwatch.project.Aircraft.Coordinate
import org.airwatch.project.Aircraft.airCrafts
import org.airwatch.project.Aircraft.currShowableAirCrafts
import org.airwatch.project.Filter.Filter.queries.areaQuery
import org.airwatch.project.Filter.Filter.queries.clearQueries
import org.airwatch.project.Filter.Filter.queries.countryQueries
import org.airwatch.project.Filter.Filter.queries.icao4Queries


object Filter
{
    private val pFilteredAirCrafts = mutableStateListOf<AirCraft>()
    val filteredAirCrafts: MutableList<AirCraft> get() = pFilteredAirCrafts
    var isFiltering = false


    object queries
    {
        val icao4Queries = mutableListOf<String>()
        val countryQueries = mutableListOf<String>()
        val areaQuery = AreaByCoordinate(
            startPosition = Coordinate(null, null),
            endPosition = Coordinate(null, null)
        )

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
                        (areaQuery.isEmpty() || areaQuery.isInBound(it.position))
            }
        )
        isFiltering = true
        println("---")
        currShowableAirCrafts().forEach { println("*${it}") }
    }

}





