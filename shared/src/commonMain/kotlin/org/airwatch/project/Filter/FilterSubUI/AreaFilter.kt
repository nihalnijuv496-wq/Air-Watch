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
fun AreaBar()
{


    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        TextBoxForDouble(
            onValueChange = { value ->
                Filter.queries.areaQuery.startPosition.latitude = value
            },
            label = {Text("starting latitude: ")}
        )

        TextBoxForDouble(
            onValueChange = { value ->
                Filter.queries.areaQuery.endPosition.latitude = value
            },
            label = {Text("ending latitude: ")}
        )

        TextBoxForDouble(
            onValueChange = { value ->
                Filter.queries.areaQuery.startPosition.longitude = value
            },
            label = {Text("starting longitude: ")}
        )
        
        TextBoxForDouble(
            onValueChange = { value ->
                Filter.queries.areaQuery.endPosition.longitude = value
            },
            label = {Text("ending longitude: ")}
        )
    }

}
