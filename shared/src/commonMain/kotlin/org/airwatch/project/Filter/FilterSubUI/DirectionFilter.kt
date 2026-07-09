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
fun DirectionBar() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        TextBoxForDouble(
            onValueChange = { value ->
                Filter.Queries.angleQuery.minAngle = value
            },
            label = { Text("Starting angle: ") },
            initialVal = Filter.Queries.angleQuery.minAngle?.toString()
        )

        TextBoxForDouble(
            onValueChange = { value ->
                Filter.Queries.angleQuery.maxAngle = value
            },
            label = { Text("Ending angle: ") },
            initialVal = Filter.Queries.angleQuery.maxAngle?.toString()
        )

    }
}