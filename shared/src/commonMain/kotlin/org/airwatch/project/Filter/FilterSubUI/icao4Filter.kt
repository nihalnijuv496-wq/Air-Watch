package org.airwatch.project.Filter.FilterSubUI

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.airwatch.project.Filter.filterAirCraftsByICAO4
import org.airwatch.project.Filter.icao4Queries
import org.airwatch.project.UIComponents.CheckBox


@Composable
fun ICAO4Bar(data: List<String>)
{
    var query by remember { mutableStateOf("") }
    val filteredSuggestions by remember {
        derivedStateOf {
            data.filter {
                it.contains(query, ignoreCase = true)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize())
    {
        ICAO4SearchBar(data)

        Button(
            onClick = { icao4Queries.clear()
                      filterAirCraftsByICAO4()},
            content = { Text("Clear") },
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ICAO4SearchBar(suggestions: List<String>) {
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val filteredSuggestions by remember {
        derivedStateOf {
            suggestions.filter { it.contains(query, ignoreCase = true) }
        }
    }

    DockedSearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = 0.8f),
        expanded = expanded,
        onExpandedChange = { expanded = it },
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = { query = it },
                onSearch = { expanded = false }, // Collapses bar on search submit
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text("Search here...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                }
            )
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {

            items(filteredSuggestions) { suggestion ->
                ListItem(
                    headlineContent = {
                            CheckBox(text = suggestion ,onCheckedChange = {
                                icao4Queries.add(suggestion)
                                filterAirCraftsByICAO4()
                            }).Draw()
                    }
                )
            }
        }
    }
}