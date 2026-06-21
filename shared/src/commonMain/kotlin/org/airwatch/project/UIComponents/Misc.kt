package org.airwatch.project.UIComponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RowDivider()
{
    HorizontalDivider(thickness = 3.dp, color = secondaryColor)
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

@Composable
fun SideBar(isVisible: Boolean, modifier: Modifier, content: @Composable AnimatedVisibilityScope.()-> Unit)
{
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(initialOffsetX = { -it }),
        exit = slideOutHorizontally(targetOffsetX = { -it }),
    )
    {
        Box(modifier = modifier
            .background(Color.Red)
            .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ){})
        {
            content()
        }
    }
}