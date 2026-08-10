package org.airwatch.project.Filter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.airwatch.project.Aircraft.AirCraft
import org.airwatch.project.Aircraft.AltitudeRange
import org.airwatch.project.Aircraft.AngleRange
import org.airwatch.project.Aircraft.AreaRangeByCoordinate
import org.airwatch.project.Aircraft.Coordinate
import org.airwatch.project.Aircraft.VelocityRange
import org.airwatch.project.Filter.FilterViewModel.Queries.altitudeQuery
import org.airwatch.project.Filter.FilterViewModel.Queries.angleQuery
import org.airwatch.project.Filter.FilterViewModel.Queries.areaQuery
import org.airwatch.project.Filter.FilterViewModel.Queries.clearQueries
import org.airwatch.project.Filter.FilterViewModel.Queries.countryQueries
import org.airwatch.project.Filter.FilterViewModel.Queries.icao4Queries
import org.airwatch.project.Filter.FilterViewModel.Queries.velocityQuery


class FilterViewModel : ViewModel()
{
    private val pFilteredAirCrafts = mutableStateListOf<AirCraft>()
    val filteredAirCrafts: MutableList<AirCraft> get() = pFilteredAirCrafts
    var isFiltering by mutableStateOf(false)
        private set



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
            AltitudeRange(startValue = null, endValue = null) // use minAltitude and maxAltitude to access
        ).value

        val velocityQuery = mutableStateOf(
            VelocityRange(
                startValue = null,
                endValue = null
            ) // use minVelocity and maxVelocity to access
        ).value

        val angleQuery = mutableStateOf(
            AngleRange(startValue = null, endValue = null) // use minAngle and maxAngle to access
        ).value

        fun clearQueries()
        {
            icao4Queries.clear()
            countryQueries.clear()
            areaQuery.clear()
            altitudeQuery.clear()
            velocityQuery.clear()
            angleQuery.clear()
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

    fun filterAll(airCrafts : List<AirCraft>) {
        setFilteredAirCraft(
            airCrafts.filter{
                (icao4Queries.isEmpty() || it.icao24 in icao4Queries) &&
                        (countryQueries.isEmpty() || it.originCountry in countryQueries) &&
                        (areaQuery.isEmpty() || areaQuery.isInBound(it.position)) &&
                        (altitudeQuery.isEmpty() || altitudeQuery.isInBound(it.baroAltitude)) &&
                        (velocityQuery.isEmpty() || velocityQuery.isInBound(it.velocity)) &&
                        (angleQuery.isEmpty() || angleQuery.isInBound(it.trueTrack))
            }
        )

        isFiltering = true
    }

}





