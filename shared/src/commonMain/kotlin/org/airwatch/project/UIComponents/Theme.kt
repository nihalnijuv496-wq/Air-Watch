package org.airwatch.project.UIComponents

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val secondaryColor = Color(209, 209, 209)
val textColor = Color.Black

object RadarColors {
    val Background    = Color(0xFF0D1512)
    val Surface       = Color(0xFF16211C)
    val SurfaceRaised = Color(0xFF1E2C25)
    val Border        = Color(0xFF2A3B33)
    val Amber         = Color(0xFFE8A33D)
    val AmberDim      = Color(0xFF9C6E2B)
    val Teal          = Color(0xFF6FA8A0)
    val TextPrimary   = Color(0xFFEDEAE2)
    val TextMuted     = Color(0xFF8B978F)
    val Alert         = Color(0xFFE85D4A)
}

private val RadarColorScheme = darkColorScheme(
    primary = RadarColors.Amber,
    onPrimary = RadarColors.Background,
    secondary = RadarColors.Teal,
    onSecondary = RadarColors.Background,
    background = RadarColors.Background,
    onBackground = RadarColors.TextPrimary,
    surface = RadarColors.Surface,
    onSurface = RadarColors.TextPrimary,
    surfaceVariant = RadarColors.SurfaceRaised,
    onSurfaceVariant = RadarColors.TextMuted,
    outline = RadarColors.Border,
    error = RadarColors.Alert,
    onError = RadarColors.TextPrimary,
)

// Two families only: FontFamily.Default for chrome (labels, buttons, headings),
// FontFamily.Monospace for actual readouts
val AirWatchTypography = Typography(
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        letterSpacing = 0.2.sp,
        color = RadarColors.TextPrimary
    ),
    labelLarge = TextStyle( // buttons / short commands
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        letterSpacing = 0.5.sp,
        color = RadarColors.Amber
    ),
    bodyMedium = TextStyle( // general UI copy
        fontFamily = FontFamily.Default,
        fontSize = 14.sp,
        color = RadarColors.TextPrimary
    ),
    bodySmall = TextStyle( // log console / data readouts
        fontFamily = FontFamily.Monospace,
        fontSize = 12.sp,
        color = RadarColors.TextMuted
    ),
)

@Composable
fun AirWatchTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RadarColorScheme,
        typography = AirWatchTypography,
        content = content
    )
}