package org.airwatch.project.UIComponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ColumnDivider()
{
    HorizontalDivider(thickness = 3.dp, color = BorderColor)
}

@Composable
fun RowDivider()
{
    VerticalDivider(thickness = 3.dp, color = BorderColor)
}

@Composable
fun BasicButton(onClick: () -> Unit, content: @Composable RowScope.() -> Unit)
{
    Button(onClick = onClick)
    {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.labelLarge.copy(fontSize = 10.sp),
        )
        {
            content()
        }
    }
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
fun SideBar(isVisible: Boolean, modifier: Modifier, contentFun: @Composable AnimatedVisibilityScope.()-> Unit)
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
            contentFun()
        }
    }
}

@Composable
fun FilterSideBarContent()
{
    var currentFilterBar: String by remember { mutableStateOf("icao4") }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = secondaryColor))
    {
        Row(
            modifier = Modifier
                .padding((10).dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
            )
        {
            Text(
                text = "Filter",
                fontSize = 20.sp,
                color = textColor
            )
        }

        ColumnDivider()


        Row(modifier = Modifier
            .fillMaxSize())
        {
            Column(modifier = Modifier.width(IntrinsicSize.Max))
            {
                BasicButton(content = { Text("icao4") }, onClick = {currentFilterBar = "icao4"})
                BasicButton(content = { Text("OriginCountry") }, onClick = {currentFilterBar = "originCountry"})
                BasicButton(content = { Text("Area") }, onClick = {currentFilterBar = "area"})
                BasicButton(content = { Text("Altitude") }, onClick = {currentFilterBar = "altitude"})
                BasicButton(content = { Text("Velocity") }, onClick = {currentFilterBar = "velocity"})
                BasicButton(content = { Text("Direction") }, onClick = {currentFilterBar = "direction"})

                Spacer(modifier = Modifier.weight(1f))

                BasicButton(content = { Text("Direction") }, onClick = {currentFilterBar = "direction"})
            }

            RowDivider()

            Column(modifier = Modifier.fillMaxSize())
            {
                when(currentFilterBar)
                {
                     "icao4" -> {

                    }

                    "originCountry" -> {

                    }

                    "area" -> {

                    }

                    "altitude" -> {

                    }

                    "velocity" -> {

                    }

                    "direction" -> {

                    }

                    else -> {
                        currentFilterBar = "icao4"
                    }


                }
            }

        }

    }
}