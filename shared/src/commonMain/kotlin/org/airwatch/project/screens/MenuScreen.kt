package org.airwatch.project.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.airwatch.project.Theme.RowDivider


@Composable
fun MenuScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.17f)
        )
        {

        }

        RowDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.65f)
        )
        {

        }

        RowDivider()

        LazyRow(
            modifier = Modifier
                .fillMaxSize()
        )
        {

        }
    }
}