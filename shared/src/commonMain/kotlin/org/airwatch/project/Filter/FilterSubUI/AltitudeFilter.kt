package org.airwatch.project.Filter.FilterSubUI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.airwatch.project.Filter.Filter
import org.airwatch.project.UIComponents.TextBoxForDouble

@Composable
fun AltitudeBar() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        TextBoxForDouble(
            onValueChange = { value ->
                Filter.Queries.altitudeQuery.minAltitude = value
            },
            label = { Text("minimum altitude: ") },
            initialVal = Filter.Queries.altitudeQuery.minAltitude?.toString()
        )

        TextBoxForDouble(
            onValueChange = { value ->
                Filter.Queries.altitudeQuery.maxAltitude = value
            },
            label = { Text("maximum altitude: ") },
            initialVal = Filter.Queries.altitudeQuery.maxAltitude?.toString()
        )

    }
}