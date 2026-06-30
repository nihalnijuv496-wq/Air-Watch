package org.airwatch.project.Filter

import androidx.compose.runtime.mutableStateListOf
import org.airwatch.project.Aircraft.AirCraft
import org.airwatch.project.Aircraft.airCrafts

private val pFilteredAirCrafts = mutableStateListOf<AirCraft>()

val filteredAirCrafts: MutableList<AirCraft> get() = pFilteredAirCrafts

val icao4Queries = mutableListOf<String>()

fun filterAirCraftsByICAO4()
{
    filteredAirCrafts.clear()
    icao4Queries.forEach { query ->
        airCrafts.forEach { airCraft ->
            if(airCraft.icao24 == query) filteredAirCrafts.add(airCraft)
        }
    }
    println(filteredAirCrafts)
}


fun clearFilter()
{
    icao4Queries.clear()
    filterAirCraftsByICAO4()
}