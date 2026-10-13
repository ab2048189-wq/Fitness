package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val StreakfitColorScheme = darkColorScheme(
  primary = StreakPrimary,
  onPrimary = StreakOnPrimary,
  primaryContainer = StreakPrimaryContainer,
  onPrimaryContainer = StreakOnPrimaryContainer,
  secondary = StreakSecondary,
  onSecondary = StreakOnSecondary,
  secondaryContainer = StreakSecondaryContainer,
  onSecondaryContainer = StreakOnSecondaryContainer,
  tertiary = StreakTertiary,
  onTertiary = StreakOnTertiary,
  tertiaryContainer = StreakTertiaryContainer,
  onTertiaryContainer = StreakOnTertiaryContainer,
  background = StreakBackground,
  onBackground = StreakOnSurface,
  surface = StreakSurface,
  onSurface = StreakOnSurface,
  surfaceVariant = StreakSurfaceVariant,
  onSurfaceVariant = StreakOnSurfaceVariant,
  surfaceContainerLowest = StreakSurfaceContainerLowest,
  surfaceContainerLow = StreakSurfaceContainerLow,
  surfaceContainer = StreakSurfaceContainer,
  surfaceContainerHigh = StreakSurfaceContainerHigh,
  surfaceContainerHighest = StreakSurfaceContainerHighest,
  surfaceBright = StreakSurfaceBright,
  outline = StreakOutline,
  outlineVariant = StreakOutlineVariant,
  error = StreakError,
  errorContainer = StreakErrorContainer,
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false, // Keep Streakfit signature neon/dark aesthetic
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = StreakfitColorScheme,
    typography = Typography,
    content = content
  )
}
