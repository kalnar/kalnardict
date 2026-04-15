package eu.kalnarapps.kalnardict.androidui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val ColorPrimaryDark = Color(0xFF00574B)
val ColorPrimary = Color(0xFF008577)
val ColorPrimaryLight = Color(0xFF4CAF50)
val ColorPrimaryDarkButton = Color(0xFF00574B)
val ColorWhite = Color.White
val ColorSecondary = Color(0xFF1976d2)
val ColorSecondaryDark = Color(0xFF004ba0)

val GradientBackground = Brush.verticalGradient(
    colorStops = arrayOf(
        0.0f to ColorPrimaryDark,
        0.38f to ColorPrimaryDark,
        1.0f to ColorPrimary
    )
)
