package com.nastolkin.shared.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val lightColors = lightColorScheme(
    primary = Color(0xFF425A45),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE4EBE2),
    onPrimaryContainer = Color(0xFF172B1A),
    background = Color.White,
    surface = Color.White,
    surfaceContainer = Color(0xFFF3F5F1),
    surfaceContainerLow = Color(0xFFF7F8F5),
    onSurface = Color(0xFF1A1D19),
    onSurfaceVariant = Color(0xFF535A50),
    outlineVariant = Color(0xFFE0E4DD)
)

private val darkColors = darkColorScheme(
    primary = Color(0xFFB4CDAF),
    onPrimary = Color(0xFF213822),
    primaryContainer = Color(0xFF304831),
    onPrimaryContainer = Color(0xFFD6EBD0),
    background = Color(0xFF121412),
    surface = Color(0xFF121412),
    surfaceContainer = Color(0xFF20241F),
    surfaceContainerLow = Color(0xFF191D18),
    onSurface = Color(0xFFE6E9E1),
    onSurfaceVariant = Color(0xFFB8C0B3),
    outlineVariant = Color(0xFF373D34)
)

@Composable
fun NastolkinTheme(themeMode: ThemeMode, content: @Composable () -> Unit) {
    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    SystemBarAppearance(isDark)
    MaterialTheme(colorScheme = if (isDark) darkColors else lightColors, content = content)
}

@Composable
private fun SystemBarAppearance(isDark: Boolean) {
    val activity = LocalActivity.current
    val view = LocalView.current
    SideEffect {
        activity?.window?.let { window ->
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = isDark.not()
                isAppearanceLightNavigationBars = isDark.not()
            }
        }
    }
}
