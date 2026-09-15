package org.airwatch.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import org.airwatch.project.UIComponents.AutoScrollLazyColumn
import org.airwatch.project.UIComponents.RadarColors

object AppLogger {
    val logs = mutableStateListOf<String>()

    fun log(message: String) {
        logs.add(message)
    }

    fun clear() {
        logs.clear()
    }
}

@Composable
fun LogConsoleScreen(modifier: Modifier = Modifier) {
    AutoScrollLazyColumn(
        items = AppLogger.logs,
        modifier = modifier.fillMaxSize()
    ) { message ->
        Text(
            text = "- $message",
            fontSize = 10.sp,
            color = RadarColors.TextPrimary
        )
    }
}