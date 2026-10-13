package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Streakfit Athletic Dark Palette
val StreakBackground = Color(0xFF070D1B) // Ultra-deep dark blue for higher contrast glass
val StreakSurface = Color(0xFF0A1224)
val StreakSurfaceContainerLowest = Color(0xFF040814)
val StreakSurfaceContainerLow = Color(0xFF0F182E)
val StreakSurfaceContainer = Color(0xFF141F3A)
val StreakSurfaceContainerHigh = Color(0xFF1C2A4C)
val StreakSurfaceContainerHighest = Color(0xFF253760)
val StreakSurfaceVariant = Color(0xFF223254)
val StreakSurfaceBright = Color(0xFF2C3E69)

// Accent Colors
val StreakPrimary = Color(0xFF4EDEA3) // Neon Mint Green
val StreakOnPrimary = Color(0xFF003824)
val StreakPrimaryContainer = Color(0xFF10B981) // Emerald Accent
val StreakOnPrimaryContainer = Color(0xFF00422B)

val StreakSecondary = Color(0xFF9DDF2E) // Kinetic Electric Lime
val StreakOnSecondary = Color(0xFF213600)
val StreakSecondaryContainer = Color(0xFF83C300)
val StreakOnSecondaryContainer = Color(0xFF304B00)
val StreakSecondaryFixed = Color(0xFFB2F746)
val StreakOnSecondaryFixed = Color(0xFF121F00)

val StreakTertiary = Color(0xFF7BD0FF) // Ice Cyan / Hydration Blue
val StreakOnTertiary = Color(0xFF00354A)
val StreakTertiaryContainer = Color(0xFF19AEE8) // Streak Freeze Ice Blue
val StreakOnTertiaryContainer = Color(0xFF003E55)
val StreakTertiaryFixed = Color(0xFFC4E7FF)

val StreakOnSurface = Color(0xFFDAE2FD)
val StreakOnSurfaceVariant = Color(0xFFA6B8CE)
val StreakOutline = Color(0xFF6E8098)
val StreakOutlineVariant = Color(0xFF2D3C55)
val StreakError = Color(0xFFFFB4AB)
val StreakErrorContainer = Color(0xFF93000A)

// iOS Liquid Glass & Specular Glow Tokens
val GlassWhiteHigh = Color(0x38FFFFFF)
val GlassWhiteMedium = Color(0x1FFFFFFF)
val GlassWhiteLow = Color(0x0EFFFFFF)
val GlassWhiteUltraLow = Color(0x06FFFFFF)

val GlassCardBackground = Color(0x8A101C33)
val GlassCardBackgroundHigh = Color(0xB8162544)
val GlassCardBackgroundLow = Color(0x660C1527)
val GlassNavBarBackground = Color(0xD6091224)

// Liquid Specular Highlight Gradients
val GlassBorderGradient = Brush.verticalGradient(
  colors = listOf(
    Color(0x55FFFFFF),
    Color(0x18FFFFFF),
    Color(0x0AFFFFFF),
    Color(0x20FFFFFF)
  )
)

val GlassBorderActiveGradient = Brush.verticalGradient(
  colors = listOf(
    Color(0x994EDEA3),
    Color(0x334EDEA3),
    Color(0x104EDEA3),
    Color(0x559DDF2E)
  )
)

val GlassGlossOverlay = Brush.verticalGradient(
  colors = listOf(
    Color(0x18FFFFFF),
    Color(0x05FFFFFF),
    Color(0x00000000)
  )
)
