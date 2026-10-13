package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.Habit
import com.example.data.StreakfitUiState
import com.example.ui.components.AmbientGlassBackdrop
import com.example.ui.components.LiquidGlassCard
import com.example.ui.components.LiquidGlassPill
import com.example.ui.theme.GlassBorderActiveGradient
import com.example.ui.theme.GlassBorderGradient
import com.example.ui.theme.GlassCardBackground
import com.example.ui.theme.GlassCardBackgroundHigh
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
import com.example.ui.theme.StreakSurfaceContainerHigh
import com.example.ui.theme.StreakSurfaceContainerLow
import com.example.ui.theme.StreakSurfaceVariant
import com.example.ui.theme.StreakTertiary
import kotlin.math.cos
import kotlin.math.sin

const val GYM_HERO_IMAGE_URL =
  "https://lh3.googleusercontent.com/aida-public/AB6AXuBAfszWz_m_7Qaz1L08wBFCfL218RH5rDPoU7E9m3T1O8DjXnY-SbtSxwjGd7VVE69OtaJ3rMV5HyRbTjMjua8BZrA7G2wHjq4taxEjUWbXS6Ae6vZgfwI93HPcpYobuZq-kbOgiXTaW4UxAmJokqJiNnJkP7EfTyPX6RwgMj-hX7j5vEM4-2JvRcmCXG8C2AN-sya9YfjD3XmuvpiWmeobaS_vI8AOby-aCGvpAn6gIASqAFwpP0DcZA"

const val RUNNER_FOOTER_IMAGE_URL =
  "https://lh3.googleusercontent.com/aida-public/AB6AXuBIpKVqgehbR18ev56aRN7z8cBWYu_jDuIBADxHs4NNsuaO-TFIOkdclLCCkX4mp2gBBb8Y05PFZ5PEnHvI1Oz0Gfv5RwWmByBE0Ixfb8L9gji3rKXNq29074zVuIAx8c6HM96mkPZ8v6JRvhwWs2bmH8QsJEaa1p6ldWbIZJf2cc0RlZ5TokFvGy-eVLI2gtAXYfpyq9ozvWu0tH5NnC1Ui3x3ysfdOLLOiBBfQ4Sp5tDH1cQ3KYo39Q"

@Composable
fun TodayScreen(
  state: StreakfitUiState,
  onStartWorkout: () -> Unit,
  onAddHabit: () -> Unit,
  onToggleHabit: (String) -> Unit,
  onAdjustWater: (Int) -> Unit,
  onCompleteWater: () -> Unit
) {
  val scrollState = rememberScrollState()

  AmbientGlassBackdrop {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(horizontal = 16.dp, vertical = 12.dp)
        .testTag("today_screen"),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Weekly Calendar Ribbon (7-Day Strip)
      WeeklyRibbonGlassCard(state)

      // 2. Daily Momentum Hero & Progress Dashboard with Fluid Dial Animation
      DailyMomentumHeroGlassCard(state)

      // 3. Planned Workout Card for Today
      PlannedWorkoutGlassCard(onStartWorkout = onStartWorkout)

      // 4. Today's Habits Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Checklist,
            contentDescription = null,
            tint = StreakPrimary,
            modifier = Modifier.size(22.dp)
          )
          Text(
            text = "Today's Habits",
            color = StreakOnSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )
        }
        LiquidGlassPill {
          Text(
            text = "Fast Log Mode",
            color = StreakOnSurfaceVariant,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }
      }

      // 5. Interactive Habit List with Liquid Glass
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        state.habits.forEach { habit ->
          HabitRowGlassItem(
            habit = habit,
            onToggle = { onToggleHabit(habit.id) },
            onAdjustWater = onAdjustWater,
            onCompleteWater = onCompleteWater
          )
        }
      }

      // 6. Motivational Footer & Quick Action Anchor
      MotivationalFooterGlassCard(onAddHabit = onAddHabit)

      Spacer(modifier = Modifier.height(96.dp))
    }
  }
}

@Composable
private fun WeeklyRibbonGlassCard(state: StreakfitUiState) {
  LiquidGlassCard(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("weekly_ribbon_card"),
    shape = RoundedCornerShape(26.dp)
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Week 42",
          color = StreakOnSurface,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Verified,
            contentDescription = null,
            tint = StreakPrimary,
            modifier = Modifier.size(15.dp)
          )
          Text(
            text = "5/7 Days Perfect",
            color = StreakPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        state.weekCalendar.forEach { day ->
          val isToday = day.isToday
          val infiniteTransition = rememberInfiniteTransition(label = "pulse_today")
          val pulseGlow by infiniteTransition.animateFloat(
            initialValue = 0.4f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
              animation = tween(1200, easing = FastOutSlowInEasing),
              repeatMode = RepeatMode.Reverse
            ),
            label = "pulse_glow"
          )

          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(16.dp))
              .background(
                when {
                  isToday -> Color(0x3D162544)
                  day.isCompleted -> Color(0x2210B981)
                  else -> Color(0x14FFFFFF)
                }
              )
              .border(
                width = 1.dp,
                brush = if (isToday) Brush.verticalGradient(
                  listOf(
                    StreakSecondary.copy(alpha = pulseGlow),
                    StreakSecondary.copy(alpha = 0.2f)
                  )
                ) else GlassBorderGradient,
                shape = RoundedCornerShape(16.dp)
              )
              .padding(vertical = 10.dp, horizontal = 2.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
              Text(
                text = day.dayLetter,
                color = if (isToday) StreakSecondary else StreakOnSurfaceVariant,
                fontSize = 11.sp,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium
              )
              Text(
                text = "${day.dayNumber}",
                color = if (isToday) StreakSecondary else StreakOnSurface,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
              )
              if (day.isCompleted) {
                Box(
                  modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(StreakPrimaryContainer)
                    .border(1.dp, Color(0x88FFFFFF), CircleShape),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = StreakOnPrimaryContainer,
                    modifier = Modifier.size(13.dp)
                  )
                }
              } else if (isToday) {
                Box(
                  modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(StreakSecondary.copy(alpha = 0.25f)),
                  contentAlignment = Alignment.Center
                ) {
                  Box(
                    modifier = Modifier
                      .size(8.dp)
                      .clip(CircleShape)
                      .background(StreakSecondary)
                  )
                }
              } else {
                Box(
                  modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(Color(0x1AFFFFFF)),
                  contentAlignment = Alignment.Center
                ) {
                  Box(
                    modifier = Modifier
                      .size(6.dp)
                      .clip(CircleShape)
                      .background(StreakOutline.copy(alpha = 0.4f))
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun DailyMomentumHeroGlassCard(state: StreakfitUiState) {
  val completedCount = state.habits.count { it.isCompleted }
  val totalCount = state.habits.size.coerceAtLeast(1)
  val targetFraction = (completedCount.toFloat() / totalCount.toFloat())

  val animatedProgress by animateFloatAsState(
    targetValue = targetFraction,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioLowBouncy,
      stiffness = Spring.StiffnessLow
    ),
    label = "gauge_progress_spring"
  )

  val animatedPercentNumber by animateIntAsState(
    targetValue = (targetFraction * 100).toInt(),
    animationSpec = tween(700),
    label = "gauge_percent_number"
  )

  LiquidGlassCard(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("daily_momentum_hero_card"),
    shape = RoundedCornerShape(28.dp)
  ) {
    Column(
      modifier = Modifier.padding(18.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          LiquidGlassPill(isActive = true) {
            Row(
              modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = null,
                tint = StreakSecondary,
                modifier = Modifier.size(14.dp)
              )
              Text(
                text = "TODAY'S TARGET",
                color = StreakSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
              )
            }
          }

          Text(
            text = "On Track!",
            color = StreakOnSurface,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 2.dp)
          )

          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "$completedCount of $totalCount ",
              color = StreakPrimary,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "habits crushed today",
              color = StreakOnSurfaceVariant,
              fontSize = 13.sp
            )
          }

          Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 6.dp)
          ) {
            LiquidGlassPill {
              Row(
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.LocalFireDepartment,
                  contentDescription = null,
                  tint = StreakSecondary,
                  modifier = Modifier.size(14.dp)
                )
                Text(
                  text = "540 kcal",
                  color = StreakOnSurface,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }

            LiquidGlassPill {
              Row(
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Timer,
                  contentDescription = null,
                  tint = StreakTertiary,
                  modifier = Modifier.size(14.dp)
                )
                Text(
                  text = "38 min",
                  color = StreakOnSurface,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }
          }
        }

        // Animated Tactile Liquid Glass Gauge Dial
        Box(
          modifier = Modifier.size(112.dp),
          contentAlignment = Alignment.Center
        ) {
          Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeW = 10.dp.toPx()
            val radius = (size.minDimension - strokeW) / 2f
            val center = Offset(size.width / 2f, size.height / 2f)

            // Background glass track
            drawCircle(
              color = Color(0x2B162544),
              radius = radius,
              center = center,
              style = Stroke(width = strokeW)
            )

            // Neon gradient animated progress arc
            drawArc(
              brush = Brush.sweepGradient(
                colors = listOf(
                  StreakPrimary,
                  StreakSecondary,
                  StreakTertiary,
                  StreakPrimary
                )
              ),
              startAngle = -90f,
              sweepAngle = 360f * animatedProgress,
              useCenter = false,
              style = Stroke(width = strokeW, cap = StrokeCap.Round)
            )

            // Glowing indicator dot at arc tip
            if (animatedProgress > 0.02f) {
              val angleRad = Math.toRadians((-90.0 + (360.0 * animatedProgress)))
              val dotX = center.x + radius * cos(angleRad).toFloat()
              val dotY = center.y + radius * sin(angleRad).toFloat()

              drawCircle(
                color = Color.White,
                radius = 3.5.dp.toPx(),
                center = Offset(dotX, dotY)
              )
            }
          }

          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "$animatedPercentNumber%",
              color = StreakOnSurface,
              fontSize = 24.sp,
              fontWeight = FontWeight.ExtraBold,
              lineHeight = 26.sp
            )
            Text(
              text = "Daily Goal",
              color = StreakOnSurfaceVariant,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }

      // Streak Freeze Shield Banner (Liquid Glass Pill Card)
      LiquidGlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = Color(0x4D0C1527)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(Color(0x3319AEE8)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AcUnit,
                contentDescription = null,
                tint = StreakTertiary,
                modifier = Modifier.size(18.dp)
              )
            }

            Column {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Text(
                  text = "Streak Shield Active",
                  color = StreakOnSurface,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
                LiquidGlassPill(isActive = true) {
                  Text(
                    text = "1 Freeze",
                    color = StreakPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                  )
                }
              }
              Text(
                text = "Safeguarded for 14-day record",
                color = StreakOnSurfaceVariant,
                fontSize = 11.sp
              )
            }
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
          ) {
            Text(
              text = "Details",
              color = StreakPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = null,
              tint = StreakPrimary,
              modifier = Modifier.size(12.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun PlannedWorkoutGlassCard(onStartWorkout: () -> Unit) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()
  val buttonScale by animateFloatAsState(
    targetValue = if (isPressed) 0.95f else 1f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    label = "start_workout_scale"
  )

  LiquidGlassCard(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("planned_workout_card"),
    shape = RoundedCornerShape(28.dp)
  ) {
    Column {
      // Photo banner with liquid overlays
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(150.dp)
      ) {
        AsyncImage(
          model = GYM_HERO_IMAGE_URL,
          contentDescription = "Gym Workout Banner",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
        // Dark vignette gradient overlay
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color.Transparent,
                  Color(0x990A1224),
                  Color(0xF00A1224)
                )
              )
            )
        )

        // Top badges in liquid glass pills
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          LiquidGlassPill {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = null,
                tint = StreakSecondary,
                modifier = Modifier.size(14.dp)
              )
              Text(
                text = "PLANNED SESSION",
                color = StreakSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          LiquidGlassPill {
            Text(
              text = "Est. 45 Mins",
              color = StreakOnSurface,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
          }
        }

        // Bottom title inside image overlay
        Column(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
          Text(
            text = "Upper Body Hypertrophy",
            color = StreakOnSurface,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
          )
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text(
              text = "5 Exercises",
              color = StreakOnSurfaceVariant,
              fontSize = 12.sp
            )
            Text(
              text = "•",
              color = StreakOnSurfaceVariant,
              fontSize = 12.sp
            )
            Text(
              text = "8,400 kg Target Volume",
              color = StreakPrimary,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      // Muscle tags and CTA button
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = "Muscles:",
            color = StreakOnSurfaceVariant,
            fontSize = 11.sp
          )
          listOf("Chest", "Shoulders", "Triceps").forEach { muscle ->
            LiquidGlassPill {
              Text(
                text = muscle,
                color = StreakOnSurface,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp)
              )
            }
          }
        }

        // Primary Start CTA
        Box(
          modifier = Modifier
            .scale(buttonScale)
            .fillMaxWidth()
            .height(50.dp)
            .clip(CircleShape)
            .background(
              Brush.horizontalGradient(
                listOf(StreakPrimaryContainer, StreakPrimary)
              )
            )
            .border(1.dp, GlassBorderGradient, CircleShape)
            .clickable(
              interactionSource = interactionSource,
              indication = null,
              onClick = onStartWorkout
            )
            .testTag("start_workout_session_button"),
          contentAlignment = Alignment.Center
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = null,
              tint = StreakOnPrimaryContainer,
              modifier = Modifier.size(20.dp)
            )
            Text(
              text = "Start Workout Session",
              color = StreakOnPrimaryContainer,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}

@Composable
private fun HabitRowGlassItem(
  habit: Habit,
  onToggle: () -> Unit,
  onAdjustWater: (Int) -> Unit,
  onCompleteWater: () -> Unit
) {
  val isWater = habit.isWaterStepper
  val isDone = habit.isCompleted

  // Animated Water Bar Fill
  val waterProgressFraction by animateFloatAsState(
    targetValue = (habit.currentValue.toFloat() / 2500f).coerceIn(0f, 1f),
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessMedium
    ),
    label = "water_fill_spring"
  )

  // Animated Stepper Touch Scale
  val plusInteraction = remember { MutableInteractionSource() }
  val isPlusPressed by plusInteraction.collectIsPressedAsState()
  val plusScale by animateFloatAsState(if (isPlusPressed) 0.85f else 1f, label = "plus_scale")

  val minusInteraction = remember { MutableInteractionSource() }
  val isMinusPressed by minusInteraction.collectIsPressedAsState()
  val minusScale by animateFloatAsState(if (isMinusPressed) 0.85f else 1f, label = "minus_scale")

  val checkInteraction = remember { MutableInteractionSource() }
  val isCheckPressed by checkInteraction.collectIsPressedAsState()
  val checkScale by animateFloatAsState(if (isCheckPressed) 0.85f else 1f, label = "check_scale")

  LiquidGlassCard(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("habit_item_${habit.id}"),
    shape = RoundedCornerShape(22.dp),
    backgroundColor = if (isDone && !isWater) Color(0x3D081223) else GlassCardBackground
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Icon Box
        Box(
          modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
              when (habit.colorTone) {
                "cyan" -> Color(0x3319AEE8)
                "amber" -> Color(0x339DDF2E)
                else -> Color(0x3310B981)
              }
            )
            .border(1.dp, GlassBorderGradient, RoundedCornerShape(14.dp)),
          contentAlignment = Alignment.Center
        ) {
          Text(text = habit.icon, fontSize = 21.sp)
        }

        Column(modifier = Modifier.weight(1f)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text(
              text = habit.title,
              color = if (isDone && !isWater) StreakOnSurfaceVariant else StreakOnSurface,
              fontSize = 14.sp,
              fontWeight = FontWeight.SemiBold,
              textDecoration = if (isDone && !isWater) TextDecoration.LineThrough else TextDecoration.None
            )
            LiquidGlassPill(isActive = true) {
              Text(
                text = "🔥 ${habit.streakDays}",
                color = StreakSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
              )
            }
          }

          if (isWater) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              modifier = Modifier.padding(top = 5.dp)
            ) {
              Text(
                text = "${"%,d".format(habit.currentValue)} / 2,500 ml",
                color = StreakOnSurfaceVariant,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
              )
              // Fluid Liquid Progress Bar with sheen
              Box(
                modifier = Modifier
                  .width(68.dp)
                  .height(8.dp)
                  .clip(CircleShape)
                  .background(Color(0x33162544))
                  .border(0.5.dp, GlassBorderGradient, CircleShape)
              ) {
                Box(
                  modifier = Modifier
                    .fillMaxWidth(waterProgressFraction)
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(
                      Brush.horizontalGradient(
                        listOf(StreakTertiary, StreakPrimary)
                      )
                    )
                )
              }
            }
          } else if (habit.isHealthConnectSynced) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp),
              modifier = Modifier.padding(top = 3.dp)
            ) {
              Text(
                text = "${"%,d".format(habit.currentValue)} / 10,000 steps",
                color = StreakOnSurfaceVariant,
                fontSize = 12.sp
              )
              LiquidGlassPill {
                Row(
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = null,
                    tint = StreakTertiary,
                    modifier = Modifier.size(11.dp)
                  )
                  Text(
                    text = "Health Connect",
                    color = StreakOutline,
                    fontSize = 10.sp
                  )
                }
              }
            }
          } else if (habit.subtitle.isNotEmpty()) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp),
              modifier = Modifier.padding(top = 2.dp)
            ) {
              if (isDone) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = StreakPrimary,
                  modifier = Modifier.size(13.dp)
                )
              }
              Text(
                text = habit.subtitle,
                color = if (isDone) StreakPrimary else StreakOnSurfaceVariant,
                fontSize = 11.sp
              )
            }
          }
        }
      }

      // Actions
      if (isWater) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          // Minus Button
          Box(
            modifier = Modifier
              .scale(minusScale)
              .size(34.dp)
              .clip(CircleShape)
              .background(Color(0x33162544))
              .border(1.dp, GlassBorderGradient, CircleShape)
              .clickable(
                interactionSource = minusInteraction,
                indication = null,
                onClick = { onAdjustWater(-250) }
              )
              .testTag("water_minus_btn"),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Remove,
              contentDescription = "Subtract water",
              tint = StreakOnSurface,
              modifier = Modifier.size(16.dp)
            )
          }

          // Plus Button
          Box(
            modifier = Modifier
              .scale(plusScale)
              .size(34.dp)
              .clip(CircleShape)
              .background(Color(0x33162544))
              .border(1.dp, GlassBorderGradient, CircleShape)
              .clickable(
                interactionSource = plusInteraction,
                indication = null,
                onClick = { onAdjustWater(250) }
              )
              .testTag("water_plus_btn"),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = "Add water",
              tint = StreakPrimary,
              modifier = Modifier.size(16.dp)
            )
          }

          // Complete Button
          Box(
            modifier = Modifier
              .scale(checkScale)
              .size(34.dp)
              .clip(CircleShape)
              .background(
                if (isDone) StreakPrimaryContainer.copy(alpha = 0.35f)
                else Color(0x33162544)
              )
              .border(
                1.dp,
                if (isDone) GlassBorderActiveGradient else GlassBorderGradient,
                CircleShape
              )
              .clickable(
                interactionSource = checkInteraction,
                indication = null,
                onClick = onCompleteWater
              )
              .testTag("water_check_btn"),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = "Complete water",
              tint = if (isDone) StreakPrimary else StreakOutline,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      } else {
        // Toggle Habit Checkbox with spring bounce
        val toggleInteraction = remember { MutableInteractionSource() }
        val isTogglePressed by toggleInteraction.collectIsPressedAsState()
        val toggleScale by animateFloatAsState(
          targetValue = if (isTogglePressed) 0.85f else 1f,
          animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
          label = "toggle_scale"
        )

        Box(
          modifier = Modifier
            .scale(toggleScale)
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
            .clickable(
              interactionSource = toggleInteraction,
              indication = null,
              onClick = onToggle
            )
            .testTag("habit_toggle_${habit.id}"),
          contentAlignment = Alignment.Center
        ) {
          if (isDone) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = "Completed",
              tint = StreakOnPrimaryContainer,
              modifier = Modifier.size(18.dp)
            )
          } else {
            Icon(
              imageVector = Icons.Default.RadioButtonUnchecked,
              contentDescription = "Pending",
              tint = StreakOnSurfaceVariant,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun MotivationalFooterGlassCard(onAddHabit: () -> Unit) {
  LiquidGlassCard(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("motivational_footer_card"),
    shape = RoundedCornerShape(26.dp)
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
        AsyncImage(
          model = RUNNER_FOOTER_IMAGE_URL,
          contentDescription = "Runner Motivation",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .border(1.5.dp, GlassBorderActiveGradient, CircleShape)
        )

        Column {
          Text(
            text = "Keep the Flame Alive",
            color = StreakOnSurface,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Log before midnight to keep 14d streak",
            color = StreakOnSurfaceVariant,
            fontSize = 11.sp
          )
        }
      }

      LiquidGlassPill(
        onClick = onAddHabit,
        modifier = Modifier.testTag("quick_add_habit_button")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = StreakPrimary,
            modifier = Modifier.size(16.dp)
          )
          Text(
            text = "Add Item",
            color = StreakPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}
