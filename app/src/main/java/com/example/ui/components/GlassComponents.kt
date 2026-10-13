package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.GlassBorderActiveGradient
import com.example.ui.theme.GlassBorderGradient
import com.example.ui.theme.GlassCardBackground
import com.example.ui.theme.GlassGlossOverlay
import com.example.ui.theme.StreakBackground
import com.example.ui.theme.StreakPrimary
import com.example.ui.theme.StreakSecondary
import com.example.ui.theme.StreakTertiary

@Composable
fun AmbientGlassBackdrop(
  modifier: Modifier = Modifier,
  content: @Composable BoxScope.() -> Unit
) {
  val infiniteTransition = rememberInfiniteTransition(label = "ambient_orbs")
  val orb1OffsetY by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 60f,
    animationSpec = infiniteRepeatable(
      animation = tween(8000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "orb1_anim"
  )
  val orb2OffsetY by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = -50f,
    animationSpec = infiniteRepeatable(
      animation = tween(9500, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "orb2_anim"
  )

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(StreakBackground)
  ) {
    // Ambient glowing neon liquid orbs blurred in background
    Canvas(modifier = Modifier.fillMaxSize()) {
      val w = size.width
      val h = size.height

      // Mint green glowing orb in top-right
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(
            StreakPrimary.copy(alpha = 0.16f),
            StreakPrimary.copy(alpha = 0.06f),
            Color.Transparent
          ),
          center = Offset(w * 0.82f, (h * 0.16f) + orb1OffsetY),
          radius = w * 0.65f
        )
      )

      // Ice blue glowing orb in middle-left
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(
            StreakTertiary.copy(alpha = 0.13f),
            StreakTertiary.copy(alpha = 0.04f),
            Color.Transparent
          ),
          center = Offset(w * 0.15f, (h * 0.48f) + orb2OffsetY),
          radius = w * 0.7f
        )
      )

      // Electric lime subtle orb near bottom
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(
            StreakSecondary.copy(alpha = 0.11f),
            StreakSecondary.copy(alpha = 0.03f),
            Color.Transparent
          ),
          center = Offset(w * 0.72f, h * 0.82f),
          radius = w * 0.6f
        )
      )
    }

    content()
  }
}

@Composable
fun LiquidGlassCard(
  modifier: Modifier = Modifier,
  shape: Shape = RoundedCornerShape(24.dp),
  backgroundColor: Color = GlassCardBackground,
  borderBrush: Brush = GlassBorderGradient,
  borderWidth: Dp = 1.dp,
  content: @Composable BoxScope.() -> Unit
) {
  Box(
    modifier = modifier
      .clip(shape)
      .background(backgroundColor)
      .border(borderWidth, borderBrush, shape)
  ) {
    // Subtle top glossy specular highlight
    Box(
      modifier = Modifier
        .matchParentSize()
        .background(GlassGlossOverlay)
    )
    content()
  }
}

@Composable
fun LiquidGlassPill(
  modifier: Modifier = Modifier,
  isActive: Boolean = false,
  onClick: (() -> Unit)? = null,
  content: @Composable BoxScope.() -> Unit
) {
  val shape = CircleShape
  val borderBrush = if (isActive) GlassBorderActiveGradient else GlassBorderGradient
  val bg = if (isActive) StreakPrimary.copy(alpha = 0.22f) else Color(0x33162544)

  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()
  val scale = if (isPressed) 0.94f else 1f

  Box(
    modifier = modifier
      .scale(scale)
      .clip(shape)
      .background(bg)
      .border(1.dp, borderBrush, shape)
      .then(
        if (onClick != null) {
          Modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
          )
        } else Modifier
      )
  ) {
    content()
  }
}
