package org.airwatch.project.Filter.FilterSubUI
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.airwatch.project.Aircraft.AreaRangeByCoordinate
import org.airwatch.project.UIComponents.TextBoxForDouble


@Composable
fun AreaBar(areaQuery: AreaRangeByCoordinate)
{


    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        TextBoxForDouble(
            onValueChange = { value ->
                areaQuery.startPosition.latitude = value
            },
            label = {Text("starting latitude: ")},
            initialVal = areaQuery.startPosition.latitude?.toString()
        )

        TextBoxForDouble(
            onValueChange = { value ->
                areaQuery.endPosition.latitude = value
            },
            label = {Text("ending latitude: ")},
            initialVal = areaQuery.endPosition.latitude?.toString()
        )

        TextBoxForDouble(
            onValueChange = { value ->
                areaQuery.startPosition.longitude = value
            },
            label = {Text("starting longitude: ")},
            initialVal = areaQuery.startPosition.longitude?.toString()
        )
        
        TextBoxForDouble(
            onValueChange = { value ->
                areaQuery.endPosition.longitude = value
            },
            label = {Text("ending longitude: ")},
            initialVal = areaQuery.endPosition.longitude?.toString()
        )
    }

}
