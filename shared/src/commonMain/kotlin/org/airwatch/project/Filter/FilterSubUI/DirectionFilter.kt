package org.airwatch.project.Filter.FilterSubUI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.airwatch.project.Aircraft.AngleRange
import org.airwatch.project.UIComponents.TextBoxForDouble

@Composable
fun DirectionBar(angleQuery: AngleRange) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        TextBoxForDouble(
            onValueChange = { value ->
                angleQuery.minAngle = value
            },
            label = { Text("Starting angle: ") },
            initialVal = angleQuery.minAngle?.toString()
        )

        TextBoxForDouble(
            onValueChange = { value ->
                angleQuery.maxAngle = value
            },
            label = { Text("Ending angle: ") },
            initialVal = angleQuery.maxAngle?.toString()
        )

    }
}