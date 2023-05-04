package unist.pjs.the.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColors(
    primary = main,
    primaryVariant = gray,
    secondary = Color.Black,
    onPrimary = Color.White,
    onSecondary = text_gray,
    secondaryVariant = main_text,
    onSurface = light_gray
)

@Composable
fun TheTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = LightColors,
        content = content
    )
}