package com.example.codeleapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Configuramos el esquema de colores oscuro con tus variables neón
private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    secondary = NeonPurple,
    tertiary = NeonYellow,
    background = BackgroundDark,
    surface = CardBackground,
    onPrimary = BackgroundDark,
    onBackground = androidx.compose.ui.graphics.Color.White,
    onSurface = androidx.compose.ui.graphics.Color.White
)

@Composable
fun CodeleAppTheme(
    // Forzamos el tema oscuro para que siempre se vea el estilo Neón
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Esto hace que la barra de estado (donde sale la hora y batería) sea del color del fondo
            window.statusBarColor = BackgroundDark.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}