package com.example.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.SwipeRight
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.HabitMetricType
import com.example.ui.components.AmbientGlassBackdrop
import com.example.ui.components.LiquidGlassCard
import com.example.ui.components.LiquidGlassPill
import com.example.ui.components.StreakfitTopBar
import com.example.ui.theme.GlassBorderActiveGradient
import com.example.ui.theme.GlassBorderGradient
import com.example.ui.theme.StreakBackground
import com.example.ui.theme.StreakOnPrimary
import com.example.ui.theme.StreakOnPrimaryContainer
import com.example.ui.theme.StreakOnSurface
import com.example.ui.theme.StreakOnSurfaceVariant
import com.example.ui.theme.StreakOutline
import com.example.ui.theme.StreakPrimary
import com.example.ui.theme.StreakPrimaryContainer
import com.example.ui.theme.StreakSecondary
import com.example.ui.theme.StreakSecondaryContainer
import com.example.ui.theme.StreakSurfaceBright
import com.example.ui.theme.StreakSurfaceContainer
import com.example.ui.theme.StreakSurfaceContainerHigh
import com.example.ui.theme.StreakSurfaceContainerLow
import com.example.ui.theme.StreakSurfaceVariant
import com.example.ui.theme.StreakTertiary

const val RECOVERY_HERO_IMAGE_URL =
  "https://lh3.googleusercontent.com/aida-public/AB6AXuAd9RMBEH0iqXO2FJ0n07fs3x-FF2ZfzevFomdO8LjeadG68eCQ0n-muJ4NBjeQSHkvAXTZMmMCA1u-epiPMl3oUaa6KSm98SCX9aLC5S8NGl9rJJ3X1MfT6VqWSORCEnlc3C5aZA1fc4E5263PwtlkClsvmM2GjIrD7NW6jvg5CpS3ZKGTVoVMMlKdHeFKQZvhjzwAtMyG05E-JSJdT1XRI7EFAwCspE45ExqAqgu-dd_l1jNXPfTA1g"

@Composable
fun NewHabitScreen(
  streakDays: Int,
  onBack: () -> Unit,
  onSaveHabit: (
    title: String,
    icon: String,
    colorTone: String,
    metricType: HabitMetricType,
    targetValue: Int,
    unit: String,
    reminderTime: String,
    forgivingStreak: Boolean
  ) -> Unit
) {
  var title by remember { mutableStateOf("Evening Mobility & Foam Rolling") }
  var selectedIcon by remember { mutableStateOf("🧘") }
  var selectedColor by remember { mutableStateOf("emerald") }
  var metricType by remember { mutableStateOf(HabitMetricType.COUNT) }
  var targetValue by remember { mutableIntStateOf(20) }
  var selectedUnit by remember { mutableStateOf("Minutes (mins)") }
  var selectedCadence by remember { mutableStateOf("Specific Weekdays") }
  var smartReminderEnabled by remember { mutableStateOf(true) }
  var reminderTime by remember { mutableStateOf("08:30 PM") }
  var forgivingStreakEnabled by remember { mutableStateOf(true) }

  val activeDays = remember {
    mutableStateListOf(true, true, true, true, true, false, true)
  }

  val iconsList = listOf("💧", "🏃", "🧘", "📖", "💊", "⚡", "🥗", "💤", "🏋️", "🧘‍♀️")
  val colorList = listOf("emerald", "amber", "violet", "cyan")

  val scrollState = rememberScrollState()

  val saveInteraction = remember { MutableInteractionSource() }
  val isSavePressed by saveInteraction.collectIsPressedAsState()
  val saveScale by animateFloatAsState(if (isSavePressed) 0.94f else 1f, label = "save_btn_scale")

  AmbientGlassBackdrop {
    Column(modifier = Modifier.fillMaxSize()) {
      StreakfitTopBar(
        title = "New Habit",
        showBack = true,
        streakDays = streakDays,
        onBackClick = onBack
      )

      Column(
        modifier = Modifier
          .weight(1f)
          .verticalScroll(scrollState)
          .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // Subtitle & Templates action
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0x334EDEA3))
                .border(1.dp, GlassBorderGradient, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = StreakPrimary,
                modifier = Modifier.size(20.dp)
              )
            }
            Column {
              Text(
                text = "New Routine",
                color = StreakOnSurface,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Lock in consistency & momentum",
                color = StreakOnSurfaceVariant,
                fontSize = 11.sp
              )
            }
          }

          LiquidGlassPill(
            onClick = {},
            modifier = Modifier.testTag("templates_button")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.BookmarkBorder,
                contentDescription = null,
                tint = StreakOnSurface,
                modifier = Modifier.size(16.dp)
              )
              Text(
                text = "Templates",
                color = StreakOnSurface,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        // Habit Name Input & Live Avatar Hero in Liquid Glass
        LiquidGlassCard(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(26.dp)
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              // Live avatar badge
              Box(
                modifier = Modifier
                  .size(56.dp)
                  .clip(RoundedCornerShape(16.dp))
                  .background(StreakPrimaryContainer)
                  .border(1.5.dp, GlassBorderGradient, RoundedCornerShape(16.dp))
                  .testTag("icon_preview_badge"),
                contentAlignment = Alignment.Center
              ) {
                Text(text = selectedIcon, fontSize = 28.sp)
              }

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "HABIT TITLE",
                  color = StreakPrimary,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.ExtraBold,
                  letterSpacing = 1.2.sp
                )
                TextField(
                  value = title,
                  onValueChange = { title = it },
                  modifier = Modifier
                    .fillMaxWidth()
                    .testTag("habit_title_input"),
                  colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0x330B1629),
                    unfocusedContainerColor = Color(0x330B1629),
                    focusedTextColor = StreakOnSurface,
                    unfocusedTextColor = StreakOnSurface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                  ),
                  shape = RoundedCornerShape(12.dp),
                  singleLine = true
                )
              }
            }

            // Icon carousel
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
              Text(
                text = "CHOOSE ICON",
                color = StreakOnSurfaceVariant,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                iconsList.forEach { icon ->
                  val isSelected = icon == selectedIcon
                  val animScale by animateFloatAsState(if (isSelected) 1.1f else 1f, label = "icon_anim")
                  Box(
                    modifier = Modifier
                      .scale(animScale)
                      .size(44.dp)
                      .clip(RoundedCornerShape(12.dp))
                      .background(
                        if (isSelected) StreakPrimaryContainer
                        else Color(0x2B0B1629)
                      )
                      .border(
                        1.dp,
                        if (isSelected) GlassBorderActiveGradient else GlassBorderGradient,
                        RoundedCornerShape(12.dp)
                      )
                      .clickable { selectedIcon = icon }
                      .testTag("icon_option_$icon"),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(text = icon, fontSize = 21.sp)
                  }
                }
              }
            }

            // Color highlight
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "COLOR HIGHLIGHT",
                color = StreakOnSurfaceVariant,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
              Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                colorList.forEach { colorTag ->
                  val isSelected = colorTag == selectedColor
                  val swatchColor = when (colorTag) {
                    "emerald" -> StreakPrimaryContainer
                    "amber" -> StreakSecondary
                    "violet" -> StreakTertiary
                    else -> StreakPrimary
                  }
                  Box(
                    modifier = Modifier
                      .size(30.dp)
                      .clip(CircleShape)
                      .background(swatchColor)
                      .clickable { selectedColor = colorTag }
                      .then(
                        if (isSelected) Modifier.border(2.dp, Color.White, CircleShape)
                        else Modifier
                      )
                      .testTag("color_option_$colorTag"),
                    contentAlignment = Alignment.Center
                  ) {
                    if (isSelected) {
                      Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(15.dp)
                      )
                    }
                  }
                }
              }
            }
          }
        }

        // Visual Motivation Card
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(115.dp)
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, GlassBorderGradient, RoundedCornerShape(24.dp))
        ) {
          AsyncImage(
            model = RECOVERY_HERO_IMAGE_URL,
            contentDescription = "Recovery Motivation",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.horizontalGradient(
                  listOf(
                    StreakBackground.copy(alpha = 0.95f),
                    StreakBackground.copy(alpha = 0.7f),
                    Color.Transparent
                  )
                )
              )
          )
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(14.dp),
            verticalArrangement = Arrangement.Center
          ) {
            LiquidGlassPill(isActive = true) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.ElectricBolt,
                  contentDescription = null,
                  tint = StreakSecondary,
                  modifier = Modifier.size(12.dp)
                )
                Text(
                  text = "RECOVERY GOAL",
                  color = StreakSecondary,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
            Text(
              text = "Consistency Beats Intensity",
              color = StreakOnSurface,
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(top = 4.dp)
            )
            Text(
              text = "15 mins daily yields 91 hours of mobility this year.",
              color = StreakOnSurfaceVariant,
              fontSize = 11.sp
            )
          }
        }

        // Habit Metric Type (FR-1)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Habit Metric Type",
              color = StreakOnSurface,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "FR-1 Specification",
              color = StreakPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            MetricTypeGlassCard(
              title = "Check-in",
              subtitle = "Yes / No",
              icon = Icons.Default.CheckCircle,
              isSelected = metricType == HabitMetricType.CHECK_IN,
              modifier = Modifier.weight(1f),
              onClick = { metricType = HabitMetricType.CHECK_IN }
            )
            MetricTypeGlassCard(
              title = "Count",
              subtitle = "Reps, ml, pages",
              icon = Icons.Default.Pin,
              isSelected = metricType == HabitMetricType.COUNT,
              modifier = Modifier.weight(1f),
              onClick = { metricType = HabitMetricType.COUNT }
            )
            MetricTypeGlassCard(
              title = "Duration",
              subtitle = "Minutes timer",
              icon = Icons.Default.Timer,
              isSelected = metricType == HabitMetricType.DURATION,
              modifier = Modifier.weight(1f),
              onClick = { metricType = HabitMetricType.DURATION }
            )
          }
        }

        // Target Goal & Unit Configuration in Liquid Glass
        LiquidGlassCard(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(24.dp)
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Target Goal",
                color = StreakOnSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Daily Threshold",
                color = StreakPrimary,
                fontSize = 12.sp
              )
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Stepper Pill
              Row(
                modifier = Modifier
                  .clip(CircleShape)
                  .background(Color(0x330B1629))
                  .border(1.dp, GlassBorderGradient, CircleShape)
                .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                LiquidGlassPill(
                  onClick = { targetValue = (targetValue - 5).coerceAtLeast(1) },
                  modifier = Modifier.testTag("stepper_decrement_btn")
                ) {
                  Icon(imageVector = Icons.Default.Remove, contentDescription = null, tint = StreakOnSurface, modifier = Modifier.size(24.dp).padding(4.dp))
                }
                Text(
                  text = "$targetValue",
                  color = StreakPrimary,
                  fontSize = 26.sp,
                  fontWeight = FontWeight.Bold
                )
                LiquidGlassPill(
                  onClick = { targetValue += 5 },
                  modifier = Modifier.testTag("stepper_increment_btn")
                ) {
                  Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = StreakOnSurface, modifier = Modifier.size(24.dp).padding(4.dp))
                }
              }

              // Unit selector
              LiquidGlassCard(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                backgroundColor = Color(0x330B1629)
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column {
                    Text(text = "UNIT", color = StreakOnSurfaceVariant, fontSize = 10.sp)
                    Text(text = selectedUnit, color = StreakOnSurface, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                  }
                  Icon(imageVector = Icons.Default.UnfoldMore, contentDescription = null, tint = StreakOutline)
                }
              }
            }

            // Preset pills
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Text(text = "Presets:", color = StreakOnSurfaceVariant, fontSize = 11.sp)
              listOf(10, 20, 30, 45).forEach { preset ->
                val isSelected = targetValue == preset
                LiquidGlassPill(
                  isActive = isSelected,
                  onClick = { targetValue = preset }
                ) {
                  Text(
                    text = "${preset}m",
                    color = if (isSelected) StreakOnPrimaryContainer else StreakOnSurface,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                  )
                }
              }
            }
          }
        }

        // Frequency & Cadence (FR-2)
        LiquidGlassCard(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(24.dp)
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Frequency & Cadence",
                color = StreakOnSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "FR-2",
                color = StreakSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              listOf("Every Day", "Specific Weekdays", "N Times / Wk").forEach { mode ->
                val isSelected = selectedCadence == mode
                LiquidGlassPill(
                  isActive = isSelected,
                  onClick = { selectedCadence = mode }
                ) {
                  Text(
                    text = mode,
                    color = if (isSelected) StreakOnPrimaryContainer else StreakOnSurfaceVariant,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                  )
                }
              }
            }

            // 7 Days Chips
            Text(
              text = "ACTIVE DAYS",
              color = StreakOnSurfaceVariant,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(top = 4.dp)
            )

            val dayLetters = listOf("M", "T", "W", "T", "F", "S", "S")
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              dayLetters.forEachIndexed { idx, letter ->
                val active = activeDays[idx]
                Box(
                  modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                      if (active) StreakPrimaryContainer
                      else Color(0x2B0B1629)
                    )
                    .border(
                      1.dp,
                      if (active) GlassBorderActiveGradient else GlassBorderGradient,
                      RoundedCornerShape(10.dp)
                    )
                    .clickable { activeDays[idx] = !activeDays[idx] }
                    .padding(vertical = 8.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                      text = letter,
                      color = if (active) StreakOnPrimaryContainer else StreakOnSurfaceVariant,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold
                    )
                    Icon(
                      imageVector = if (active) Icons.Default.Check else Icons.Default.Remove,
                      contentDescription = null,
                      tint = if (active) StreakOnPrimaryContainer else StreakOutline,
                      modifier = Modifier.size(12.dp)
                    )
                  }
                }
              }
            }
          }
        }

        // Smart Reminders & Forgiving Streak Bento
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .size(36.dp)
                      .clip(CircleShape)
                      .background(Color(0x334EDEA3)),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.NotificationsActive,
                      contentDescription = null,
                      tint = StreakPrimary,
                      modifier = Modifier.size(18.dp)
                    )
                  }
                  Column {
                    Text(text = "Smart Reminder", color = StreakOnSurface, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Notification with quick log", color = StreakOnSurfaceVariant, fontSize = 11.sp)
                  }
                }
                Switch(
                  checked = smartReminderEnabled,
                  onCheckedChange = { smartReminderEnabled = it },
                  colors = SwitchDefaults.colors(
                    checkedThumbColor = StreakOnPrimaryContainer,
                    checkedTrackColor = StreakPrimaryContainer
                  )
                )
              }

              // Time banner
              LiquidGlassCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                backgroundColor = Color(0x330B1629)
              ) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                  ) {
                    Icon(imageVector = Icons.Default.Alarm, contentDescription = null, tint = StreakPrimary, modifier = Modifier.size(18.dp))
                    Text(text = reminderTime, color = StreakOnSurface, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                  }
                  LiquidGlassPill(
                    onClick = {
                      reminderTime = if (reminderTime == "08:30 PM") "09:00 PM" else "08:30 PM"
                    }
                  ) {
                    Text(
                      text = "Change",
                      color = StreakOnSurface,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                  }
                }
              }

              // Action preview
              LiquidGlassPill {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                  ) {
                    Icon(imageVector = Icons.Default.SwipeRight, contentDescription = null, tint = StreakSecondary, modifier = Modifier.size(16.dp))
                    Text(text = "Quick Snooze (15m) or One-Tap Done", color = StreakOnSurfaceVariant, fontSize = 11.sp)
                  }
                  Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = StreakOutline, modifier = Modifier.size(14.dp))
                }
              }
            }
          }

          // Forgiving Streak Freeze Card
          LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Top
            ) {
              Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0x3319AEE8)),
                  contentAlignment = Alignment.Center
                ) {
                  Text(text = "❄", fontSize = 16.sp)
                }
                Column {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                  ) {
                    Text(text = "Forgiving Streak", color = StreakOnSurface, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    LiquidGlassPill(isActive = true) {
                      Text(
                        text = "P0",
                        color = StreakSecondary,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                      )
                    }
                  }
                  Text(
                    text = "Automatically applies 1 weekly streak freeze so a missed gym day doesn't reset months of hard work.",
                    color = StreakOnSurfaceVariant,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    modifier = Modifier.padding(top = 2.dp)
                  )
                }
              }
              Switch(
                checked = forgivingStreakEnabled,
                onCheckedChange = { forgivingStreakEnabled = it },
                colors = SwitchDefaults.colors(
                  checkedThumbColor = StreakOnPrimaryContainer,
                  checkedTrackColor = StreakPrimaryContainer
                )
              )
            }
          }
        }

        // Primary Kinetic CTA Button with liquid glass shine
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .scale(saveScale)
              .fillMaxWidth()
              .height(52.dp)
              .clip(CircleShape)
              .background(
                Brush.horizontalGradient(
                  listOf(StreakPrimaryContainer, StreakPrimary)
                )
              )
              .border(1.dp, GlassBorderGradient, CircleShape)
              .clickable(
                interactionSource = saveInteraction,
                indication = null,
                onClick = {
                  onSaveHabit(
                    title,
                    selectedIcon,
                    selectedColor,
                    metricType,
                    targetValue,
                    selectedUnit,
                    reminderTime,
                    forgivingStreakEnabled
                  )
                }
              )
              .testTag("save_and_add_to_today_button"),
            contentAlignment = Alignment.Center
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(imageVector = Icons.Default.ElectricBolt, contentDescription = null, tint = StreakOnPrimaryContainer, modifier = Modifier.size(20.dp))
              Text(text = "Save & Add to Today", color = StreakOnPrimaryContainer, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
          }
          Text(
            text = "Ready to start streak #1 upon saving.",
            color = StreakOutline,
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 8.dp)
          )
        }

        Spacer(modifier = Modifier.height(48.dp))
      }
    }
  }
}

@Composable
private fun MetricTypeGlassCard(
  title: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  isSelected: Boolean,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(16.dp))
      .background(
        if (isSelected) StreakPrimaryContainer
        else Color(0x33141F3A)
      )
      .border(
        1.dp,
        if (isSelected) GlassBorderActiveGradient else GlassBorderGradient,
        RoundedCornerShape(16.dp)
      )
      .clickable(onClick = onClick)
      .padding(vertical = 12.dp, horizontal = 6.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(
            if (isSelected) StreakOnPrimaryContainer.copy(alpha = 0.2f)
            else Color(0x33FFFFFF)
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = if (isSelected) StreakOnPrimaryContainer else StreakOnSurface,
          modifier = Modifier.size(18.dp)
        )
      }
      Text(
        text = title,
        color = if (isSelected) StreakOnPrimaryContainer else StreakOnSurface,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = subtitle,
        color = if (isSelected) StreakOnPrimaryContainer.copy(alpha = 0.85f) else StreakOutline,
        fontSize = 10.sp
      )
    }
  }
}
