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

// Esquema de Cores para o Tema Claro
private val LightColorScheme = lightColorScheme(
    primary = AzulGradienteFim, // Cor principal (usamos o fim do gradiente azul como base)
    secondary = AzulGradienteInicio, // Cor secundária
    background = FundoClaro, // Fundo principal das telas
    surface = CardBranco, // Fundo de Cards, BottomAppBar, etc.
    error = VermelhoGradienteInicio, // Cor para erros/alertas
    onPrimary = Branco, // Texto/ícone sobre cor primária
    onSecondary = Branco, // Texto/ícone sobre cor secundária
    onBackground = TextoPrimarioClaro, // Texto principal sobre o fundo claro
    onSurface = TextoPrimarioClaro, // Texto principal sobre superfícies brancas/claras
    onSurfaceVariant = CinzaIcones, // Ícones inativos, textos secundários
    onError = Branco // Texto/ícone sobre cor de erro
)

// Esquema de Cores para o Tema Escuro
private val DarkColorScheme = darkColorScheme(
    primary = AzulGradienteFim, // Mantendo o azul como primário
    secondary = AzulGradienteInicio,
    background = FundoEscuro, // Fundo escuro definido
    surface = Color(0xFF6F7788), // Um cinza um pouco mais claro que o fundo para cards (ajustar)
    error = VermelhoGradienteInicio,
    onPrimary = Branco,
    onSecondary = Branco,
    onBackground = TextoPrimarioEscuro, // Texto branco sobre fundo escuro
    onSurface = TextoPrimarioEscuro, // Texto branco sobre superfície escura
    onSurfaceVariant = Color(0xFFBFC6D4), // Um cinza mais claro para ícones inativos (ajustar)
    onError = Branco
)

@Composable
fun SalusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Agora detecta o modo do sistema
    // Dynamic color is available on Android 12+
    // dynamicColor: Boolean = true, // Manter desabilitado para usar nosso tema
    content: @Composable () -> Unit
) {
    // Seleciona o esquema de cores baseado no modo claro/escuro
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb() // Cor da barra de status
            // Ajusta a cor dos ícones da barra de status (claros no tema escuro, escuros no tema claro)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Do seu Type.kt
        shapes = Shapes,       // Do seu Shape.kt
        content = content
    )
}