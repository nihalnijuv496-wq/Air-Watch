package org.airwatch.project.Filter.FilterSubUI

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.airwatch.project.UIComponents.SearchBarFilter


@Composable
fun ICAO4Bar(data: HashSet<String>, icao4Queries: MutableList<String>)
{
    Box(modifier = Modifier.fillMaxSize())
    {
        SearchBarFilter(suggestions = data, queryList = icao4Queries)
    }

}