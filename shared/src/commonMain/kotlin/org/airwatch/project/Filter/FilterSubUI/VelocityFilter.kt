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
fun VelocityBar() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        TextBoxForDouble(
            onValueChange = { value ->
                Filter.Queries.velocityQuery.minVelocity = value
            },
            label = { Text("minimum velocity: ") },
            initialVal = Filter.Queries.velocityQuery.minVelocity?.toString()
        )

        TextBoxForDouble(
            onValueChange = { value ->
                Filter.Queries.velocityQuery.maxVelocity = value
            },
            label = { Text("maximum velocity: ") },
            initialVal = Filter.Queries.velocityQuery.maxVelocity?.toString()
        )

    }
}