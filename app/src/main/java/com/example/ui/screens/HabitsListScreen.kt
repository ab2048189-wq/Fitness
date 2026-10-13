package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StreakfitUiState
import com.example.ui.components.AmbientGlassBackdrop
import com.example.ui.components.LiquidGlassCard
import com.example.ui.components.LiquidGlassPill
import com.example.ui.theme.GlassBorderActiveGradient
import com.example.ui.theme.GlassBorderGradient
import com.example.ui.theme.StreakBackground
import com.example.ui.theme.StreakOnPrimaryContainer
import com.example.ui.theme.StreakOnSurface
import com.example.ui.theme.StreakOnSurfaceVariant
import com.example.ui.theme.StreakPrimary
import com.example.ui.theme.StreakPrimaryContainer
import com.example.ui.theme.StreakSecondary

@Composable
fun HabitsListScreen(
  state: StreakfitUiState,
  onAddNewHabit: () -> Unit,
  onToggleHabit: (String) -> Unit
) {
  val scrollState = rememberScrollState()

  AmbientGlassBackdrop {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(horizontal = 16.dp, vertical = 12.dp)
        .testTag("habits_list_screen"),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Header Hero Banner in Liquid Glass
      LiquidGlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "DAILY RITUALS",
              color = StreakPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 1.2.sp
            )
            Text(
              text = "Active Habit Tracker",
              color = StreakOnSurface,
              fontSize = 19.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "${state.habits.size} Habits tracked • 87% Consistency",
              color = StreakOnSurfaceVariant,
              fontSize = 12.sp,
              modifier = Modifier.padding(top = 2.dp)
            )
          }

          Box(
            modifier = Modifier
              .size(50.dp)
              .clip(CircleShape)
              .background(Color(0x334EDEA3))
              .border(1.5.dp, GlassBorderGradient, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Checklist,
              contentDescription = null,
              tint = StreakPrimary,
              modifier = Modifier.size(24.dp)
            )
          }
        }
      }

      // New Habit button with Liquid Glass Sheen
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .clip(CircleShape)
          .background(
            Brush.horizontalGradient(
              listOf(StreakPrimaryContainer, StreakPrimary)
            )
          )
          .border(1.dp, GlassBorderGradient, CircleShape)
          .clickable(onClick = onAddNewHabit)
          .testTag("add_new_routine_button"),
        contentAlignment = Alignment.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = StreakOnPrimaryContainer, modifier = Modifier.size(18.dp))
          Text(text = "Create New Habit Routine", color = StreakOnPrimaryContainer, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
      }

      // All Habits list in Liquid Glass
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        state.habits.forEach { habit ->
          val isDone = habit.isCompleted
          LiquidGlassCard(
            modifier = Modifier
              .fillMaxWidth()
              .testTag("habits_screen_item_${habit.id}"),
            shape = RoundedCornerShape(22.dp),
            backgroundColor = if (isDone) Color(0x33081223) else Color(0x66101C33)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
              ) {
                Box(
                  modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x334EDEA3))
                    .border(1.dp, GlassBorderGradient, RoundedCornerShape(14.dp)),
                  contentAlignment = Alignment.Center
                ) {
                  Text(text = habit.icon, fontSize = 21.sp)
                }

                Column {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                  ) {
                    Text(
                      text = habit.title,
                      color = if (isDone) StreakOnSurfaceVariant else StreakOnSurface,
                      fontSize = 14.sp,
                      fontWeight = FontWeight.SemiBold,
                      textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                    )
                    LiquidGlassPill(isActive = true) {
                      Text(
                        text = "🔥 ${habit.streakDays}d",
                        color = StreakSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                      )
                    }
                  }
                  Text(
                    text = habit.subtitle.ifEmpty { "Daily goal: ${habit.targetValue} ${habit.unit}" },
                    color = StreakOnSurfaceVariant,
                    fontSize = 11.sp
                  )
                }
              }

              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(
                    if (isDone) StreakPrimaryContainer
                    else Color(0x33162544)
                  )
                  .border(
                    1.dp,
                    if (isDone) GlassBorderActiveGradient else GlassBorderGradient,
                    CircleShape
                  )
                  .clickable { onToggleHabit(habit.id) }
                  .testTag("habits_tab_toggle_${habit.id}"),
                contentAlignment = Alignment.Center
              ) {
                if (isDone) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = StreakOnPrimaryContainer,
                    modifier = Modifier.size(18.dp)
                  )
                } else {
                  Icon(
                    imageVector = Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = StreakOnSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                  )
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(96.dp))
    }
  }
}
