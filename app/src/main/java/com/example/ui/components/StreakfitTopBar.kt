package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.GlassBorderGradient
import com.example.ui.theme.GlassGlossOverlay
import com.example.ui.theme.StreakOnSurface
import com.example.ui.theme.StreakPrimary
import com.example.ui.theme.StreakSecondary

const val PROFILE_AVATAR_URL =
  "https://lh3.googleusercontent.com/aida-public/AB6AXuAuv5Gb0ZY71s7mFsLx9DbGEr1aQNsYRkTu7mIf6mvjNX0wdBfbqvnu2zaFnuCwHKgbnDkAlHDppTmCglMo007VJnekqMUIS8ifEj0-TcbCgnY1SxEFZtUjwW0pdEnKCWOg0NkT1y4ILygb3uTHhCHf8-YSMS7nN-kVcEW_QBHqgEvj-R9NTRB7ZjB1Q5fua1iQI-qJylK-VOWDXq4MIUOLG_Y07S9bltlEoib0nPMlLEN67rFY-BvtmA"

@Composable
fun StreakfitTopBar(
  title: String,
  showBack: Boolean = false,
  streakDays: Int = 14,
  onBackClick: () -> Unit = {}
) {
  val flameAnim = rememberInfiniteTransition(label = "flame_pulse")
  val flameScale by flameAnim.animateFloat(
    initialValue = 1f,
    targetValue = 1.15f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200),
      repeatMode = RepeatMode.Reverse
    ),
    label = "flame_scale"
  )

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color(0x99091224))
      .border(
        width = 1.dp,
        brush = GlassBorderGradient,
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
      )
      .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
      .statusBarsPadding()
      .padding(horizontal = 16.dp, vertical = 10.dp)
      .testTag("top_app_bar")
  ) {
    // Glass sheen overlay
    Box(
      modifier = Modifier
        .matchParentSize()
        .background(GlassGlossOverlay)
    )

    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f, fill = false)
      ) {
        if (showBack) {
          LiquidGlassPill(
            onClick = onBackClick,
            modifier = Modifier
              .size(38.dp)
              .testTag("back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = StreakOnSurface,
              modifier = Modifier
                .size(18.dp)
                .align(Alignment.Center)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
        }

        // Stylized iOS Liquid Glass Emblem
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x334EDEA3))
            .border(1.dp, Color(0x664EDEA3), RoundedCornerShape(12.dp)),
          contentAlignment = Alignment.Center
        ) {
          Text(text = "⚡", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
          Text(
            text = "STREAKFIT",
            color = StreakPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.4.sp,
            lineHeight = 12.sp
          )
          Text(
            text = title,
            color = StreakOnSurface,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 22.sp
          )
        }
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // 14 Days Glass Fire Pill
        LiquidGlassPill(isActive = true) {
          Row(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.LocalFireDepartment,
              contentDescription = "Streak",
              tint = StreakSecondary,
              modifier = Modifier
                .size(16.dp)
                .scale(flameScale)
            )
            Text(
              text = "$streakDays Days",
              color = StreakPrimary,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        // Profile Avatar with liquid glass border
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(Color(0x33FFFFFF))
            .border(1.5.dp, GlassBorderGradient, CircleShape)
            .padding(1.5.dp)
        ) {
          AsyncImage(
            model = PROFILE_AVATAR_URL,
            contentDescription = "Profile",
            contentScale = ContentScale.Crop,
            modifier = Modifier
              .matchParentSize()
              .clip(CircleShape)
          )
        }
      }
    }
  }
}
