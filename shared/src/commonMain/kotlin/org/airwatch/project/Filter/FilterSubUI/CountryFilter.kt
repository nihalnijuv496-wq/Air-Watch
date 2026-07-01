package org.airwatch.project.Filter.FilterSubUI

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.airwatch.project.Filter.Filter
import org.airwatch.project.UIComponents.SearchBarFilter

@Composable
fun CountryBar(data: List<String>)
{
    var query by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize())
    {
        SearchBarFilter(suggestions = data, queryList = Filter.countryQueries)
    }

}