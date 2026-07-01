package org.airwatch.project.Filter

import androidx.compose.runtime.mutableStateListOf
import org.airwatch.project.Aircraft.AirCraft
import org.airwatch.project.Aircraft.airCrafts
import org.airwatch.project.Aircraft.currShowableAirCrafts
import org.airwatch.project.Filter.Filter.countryQueries
import org.airwatch.project.Filter.Filter.icao4Queries
import org.airwatch.project.Filter.Filter.setFilteredAirCraft


object Filter
{
    private val pFilteredAirCrafts = mutableStateListOf<AirCraft>()
    val filteredAirCrafts: MutableList<AirCraft> get() = pFilteredAirCrafts


    val icao4Queries = mutableListOf<String>()
    val countryQueries = mutableListOf<String>()

    fun clearFilter()
    {
        icao4Queries.clear()
        countryQueries.clear()
    }

    fun setFilteredAirCraft(newList: List<AirCraft>)
    {
        filteredAirCrafts.clear()
        newList.forEach { filteredAirCrafts.add(it) }
    }

}
fun filterAll() {
    setFilteredAirCraft(
        airCrafts.filter{
            (icao4Queries.isEmpty() || it.icao24 in icao4Queries) &&
                    (countryQueries.isEmpty() || it.originCountry in countryQueries)
        }
    )

    println("---")
    currShowableAirCrafts().forEach { println(it) }
}




