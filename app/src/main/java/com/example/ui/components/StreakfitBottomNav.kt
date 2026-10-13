package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StreakfitTab
import com.example.ui.theme.GlassBorderActiveGradient
import com.example.ui.theme.GlassBorderGradient
import com.example.ui.theme.GlassGlossOverlay
import com.example.ui.theme.GlassNavBarBackground
import com.example.ui.theme.StreakOnSurfaceVariant
import com.example.ui.theme.StreakPrimary
import com.example.ui.theme.StreakPrimaryContainer

@Composable
fun StreakfitBottomNav(
  currentTab: StreakfitTab,
  onTabSelected: (StreakfitTab) -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .navigationBarsPadding()
      .padding(horizontal = 20.dp, vertical = 12.dp)
      .testTag("bottom_navigation_bar"),
    contentAlignment = Alignment.Center
  ) {
    // Floating iOS Liquid Glass Dock
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(70.dp)
        .clip(RoundedCornerShape(36.dp))
        .background(GlassNavBarBackground)
        .border(1.dp, GlassBorderGradient, RoundedCornerShape(36.dp))
        .padding(horizontal = 8.dp)
    ) {
      // Top specular sheen
      Box(
        modifier = Modifier
          .matchParentSize()
          .background(GlassGlossOverlay)
      )

      Row(
        modifier = Modifier
          .matchParentSize()
          .padding(horizontal = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
      ) {
        NavTabItem(
          label = "Today",
          icon = Icons.Default.Today,
          isSelected = currentTab == StreakfitTab.TODAY,
          tag = "nav_tab_today",
          onClick = { onTabSelected(StreakfitTab.TODAY) }
        )
        NavTabItem(
          label = "Workouts",
          icon = Icons.Default.FitnessCenter,
          isSelected = currentTab == StreakfitTab.WORKOUTS,
          tag = "nav_tab_workouts",
          onClick = { onTabSelected(StreakfitTab.WORKOUTS) }
        )
        NavTabItem(
          label = "Habits",
          icon = Icons.Default.Checklist,
          isSelected = currentTab == StreakfitTab.HABITS,
          tag = "nav_tab_habits",
          onClick = { onTabSelected(StreakfitTab.HABITS) }
        )
        NavTabItem(
          label = "Progress",
          icon = Icons.Default.Insights,
          isSelected = currentTab == StreakfitTab.PROGRESS,
          tag = "nav_tab_progress",
          onClick = { onTabSelected(StreakfitTab.PROGRESS) }
        )
      }
    }
  }
}

@Composable
private fun NavTabItem(
  label: String,
  icon: ImageVector,
  isSelected: Boolean,
  tag: String,
  onClick: () -> Unit
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val scaleAnim by animateFloatAsState(
    targetValue = if (isPressed) 0.88f else if (isSelected) 1.05f else 1f,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessMedium
    ),
    label = "nav_item_scale"
  )

  val iconColor by animateColorAsState(
    targetValue = if (isSelected) StreakPrimary else StreakOnSurfaceVariant,
    animationSpec = spring(stiffness = Spring.StiffnessLow),
    label = "nav_item_color"
  )

  Column(
    modifier = Modifier
      .scale(scaleAnim)
      .clip(RoundedCornerShape(20.dp))
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
      )
      .padding(horizontal = 10.dp, vertical = 6.dp)
      .testTag(tag),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(3.dp)
  ) {
    // Glass Pill for Icon
    Box(
      modifier = Modifier
        .width(48.dp)
        .height(30.dp)
        .clip(CircleShape)
        .background(
          if (isSelected) StreakPrimaryContainer.copy(alpha = 0.22f)
          else Color.Transparent
        )
        .then(
          if (isSelected) Modifier.border(1.dp, GlassBorderActiveGradient, CircleShape)
          else Modifier
        ),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = iconColor,
        modifier = Modifier.size(20.dp)
      )
    }
    Text(
      text = label,
      color = iconColor,
      fontSize = 11.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
    )
  }
}
