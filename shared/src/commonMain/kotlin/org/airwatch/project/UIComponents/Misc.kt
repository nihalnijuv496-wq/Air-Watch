package org.airwatch.project.UIComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RowDivider()
{
    HorizontalDivider(thickness = 3.dp, color = Color(0xFF252525))
}

@Composable
fun ScrollableColumn(modifier: Modifier, content: @Composable ColumnScope.()-> Unit, count:Int)
{
    val scrollState = rememberScrollState()
    LaunchedEffect(count)
    {
        scrollState.animateScrollTo(scrollState.maxValue)
    }
    Column(
        modifier = modifier.verticalScroll(scrollState),
        content = content
    )
}