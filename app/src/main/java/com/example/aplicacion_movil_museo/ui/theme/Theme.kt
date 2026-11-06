package com.example.aplicacion_movil_museo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = OrangePrimary,
    secondary = OrangeSecondary,
    tertiary = OrangeTertiary,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    primaryContainer = Color(0xFF242424),   // Tarjetas principales en gris oscuro (Cero Morado)
    onPrimaryContainer = Color.White,
    secondaryContainer = Color(0xFF2C2C2C),
    onSecondaryContainer = Color.White,
    tertiaryContainer = Color(0xFF2C2C2C),
    onTertiaryContainer = Color.White,
    surfaceContainer = Color(0xFF1E1E1E),
    surfaceContainerHigh = Color(0xFF252525),
    surfaceContainerHighest = Color(0xFF2C2C2C),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = Color(0xFFE0E0E0)
)

private val LightColorScheme = darkColorScheme( // Forzamos paleta oscura elegante siempre
    primary = OrangePrimary,
    secondary = OrangeSecondary,
    tertiary = OrangeTertiary,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    primaryContainer = Color(0xFF242424),
    onPrimaryContainer = Color.White,
    secondaryContainer = Color(0xFF2C2C2C),
    onSecondaryContainer = Color.White,
    tertiaryContainer = Color(0xFF2C2C2C),
    onTertiaryContainer = Color.White,
    surfaceContainer = Color(0xFF1E1E1E),
    surfaceContainerHigh = Color(0xFF252525),
    surfaceContainerHighest = Color(0xFF2C2C2C),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = Color(0xFFE0E0E0)
)

@Composable
fun Aplicacion_movil_museoTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
