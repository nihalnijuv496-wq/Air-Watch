package org.airwatch.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import org.airwatch.project.screens.MenuScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        var screen by remember { mutableStateOf("menu")}

        when(screen)
        {
            "menu" -> MenuScreen()
        }

    }
}