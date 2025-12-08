package com.example.salus.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = AzulGradienteFim,
    secondary = AzulGradienteInicio,
    background = FundoClaro,
    surface = CardBranco,
    error = VermelhoGradienteInicio,
    onPrimary = Branco,
    onSecondary = Branco,
    onBackground = TextoPrimarioClaro,
    onSurface = TextoPrimarioClaro,
    onSurfaceVariant = CinzaIcones,
    onError = Branco
)

private val DarkColorScheme = darkColorScheme(
    primary = AzulGradienteFim,
    secondary = AzulGradienteInicio,
    background = FundoEscuro,
    surface = Color(0xFF6F7788),
    error = VermelhoGradienteInicio,
    onPrimary = Branco,
    onSecondary = Branco,
    onBackground = TextoPrimarioEscuro,
    onSurface = TextoPrimarioEscuro,
    onSurfaceVariant = Color(0xFFBFC6D4),
    onError = Branco
)

@Composable
fun SalusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}