package org.airwatch.project.Filter.FilterSubUI

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.airwatch.project.Filter.Filter
import org.airwatch.project.UIComponents.SearchBarFilter


@Composable
fun ICAO4Bar(data: List<String>)
{
    Box(modifier = Modifier.fillMaxSize())
    {
        SearchBarFilter(suggestions = data, queryList = Filter.queries.icao4Queries)
    }

}