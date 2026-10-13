package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Hardware
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StreakfitUiState
import com.example.data.WorkoutExercise
import com.example.ui.components.AmbientGlassBackdrop
import com.example.ui.components.LiquidGlassCard
import com.example.ui.components.LiquidGlassPill
import com.example.ui.components.StreakfitTopBar
import com.example.ui.theme.GlassBorderActiveGradient
import com.example.ui.theme.GlassBorderGradient
import com.example.ui.theme.GlassCardBackground
import com.example.ui.theme.GlassCardBackgroundHigh
import com.example.ui.theme.GlassNavBarBackground
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
import com.example.ui.theme.StreakSurfaceContainerHigh
import com.example.ui.theme.StreakSurfaceContainerLow
import com.example.ui.theme.StreakSurfaceVariant
import com.example.ui.theme.StreakTertiary

@Composable
fun ActiveWorkoutScreen(
  state: StreakfitUiState,
  onBack: () -> Unit,
  onAddRestTime: (Int) -> Unit,
  onSkipRestTime: () -> Unit,
  onAdjustWeight: (String, Int, Double) -> Unit,
  onAdjustReps: (String, Int, Int) -> Unit,
  onLogSet: (String, Int) -> Unit,
  onAddSet: (String) -> Unit,
  onAddExercise: () -> Unit,
  onOpenFinishModal: () -> Unit,
  onCloseFinishModal: () -> Unit,
  onCompleteWorkout: () -> Unit
) {
  val scrollState = rememberScrollState()

  val elapsedMins = state.elapsedSeconds / 60
  val elapsedSecs = state.elapsedSeconds % 60
  val elapsedStr = "%02d:%02d".format(elapsedMins, elapsedSecs)

  val restMins = state.restSeconds / 60
  val restSecs = state.restSeconds % 60
  val restStr = "%02d:%02d".format(restMins, restSecs)

  // Fluid countdown progress sweep animation
  val animatedRestSweep by animateFloatAsState(
    targetValue = (state.restSeconds.toFloat() / state.maxRestSeconds.toFloat()).coerceIn(0f, 1f),
    animationSpec = tween(900, easing = FastOutSlowInEasing),
    label = "rest_timer_sweep"
  )

  // Pulsing breath on active live timer
  val livePulseTransition = rememberInfiniteTransition(label = "live_pulse")
  val livePulseScale by livePulseTransition.animateFloat(
    initialValue = 0.85f,
    targetValue = 1.25f,
    animationSpec = infiniteRepeatable(
      animation = tween(1000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "live_pulse_scale"
  )

  AmbientGlassBackdrop {
    Column(modifier = Modifier.fillMaxSize()) {
      StreakfitTopBar(
        title = "Active Workout",
        showBack = true,
        streakDays = state.streakDays,
        onBackClick = onBack
      )

      Column(
        modifier = Modifier
          .weight(1f)
          .verticalScroll(scrollState)
          .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Top Live Session Meta & Pulse Timer
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "LIVE GYM SESSION",
              color = StreakPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 1.2.sp
            )
            Text(
              text = "${state.liveWorkoutTitle} ${state.liveWorkoutSubtitle}",
              color = StreakOnSurface,
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold
            )
          }

          LiquidGlassPill(isActive = true) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .scale(livePulseScale)
                  .clip(CircleShape)
                  .background(StreakPrimary)
              )
              Text(
                text = elapsedStr,
                color = StreakPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        // Rest Timer Liquid Glass Card with Fluid Ring Animation
        LiquidGlassCard(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("rest_timer_box"),
          shape = RoundedCornerShape(26.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              // Fluid countdown ring
              Box(
                modifier = Modifier.size(52.dp),
                contentAlignment = Alignment.Center
              ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                  val strokeW = 4.5.dp.toPx()
                  drawCircle(
                    color = Color(0x2B162544),
                    style = Stroke(strokeW)
                  )
                  drawArc(
                    brush = Brush.sweepGradient(
                      listOf(StreakPrimary, StreakSecondary, StreakTertiary, StreakPrimary)
                    ),
                    startAngle = -90f,
                    sweepAngle = 360f * animatedRestSweep,
                    useCenter = false,
                    style = Stroke(strokeW, cap = StrokeCap.Round)
                  )
                }
                Icon(
                  imageVector = Icons.Default.Timer,
                  contentDescription = null,
                  tint = StreakPrimary,
                  modifier = Modifier.size(20.dp)
                )
              }

              Column {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Text(
                    text = "REST TIMER",
                    color = StreakOnSurfaceVariant,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                  )
                  LiquidGlassPill(isActive = true) {
                    Text(
                      text = "Active",
                      color = StreakSecondary,
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                    )
                  }
                }
                Text(
                  text = restStr,
                  color = StreakOnSurface,
                  fontSize = 22.sp,
                  fontWeight = FontWeight.ExtraBold
                )
              }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              LiquidGlassPill(
                onClick = { onAddRestTime(30) },
                modifier = Modifier.testTag("add_rest_time_button")
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = StreakOnSurface,
                    modifier = Modifier.size(14.dp)
                  )
                  Text(
                    text = "30s",
                    color = StreakOnSurface,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }

              LiquidGlassPill(
                onClick = onSkipRestTime,
                modifier = Modifier.testTag("skip_rest_time_button")
              ) {
                Text(
                  text = "Skip",
                  color = StreakOnSurfaceVariant,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium,
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                )
              }
            }
          }
        }

        // Bento Bar: Total Volume & Progress
        val completedSets = state.liveExercises.flatMap { it.sets }.count { it.isCompleted }
        val totalSets = state.liveExercises.flatMap { it.sets }.size

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          LiquidGlassCard(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(20.dp)
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(CircleShape)
                  .background(Color(0x334EDEA3)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.FitnessCenter,
                  contentDescription = null,
                  tint = StreakPrimary,
                  modifier = Modifier.size(19.dp)
                )
              }
              Column {
                Text(
                  text = "Total Volume",
                  color = StreakOnSurfaceVariant,
                  fontSize = 11.sp
                )
                Row(verticalAlignment = Alignment.Bottom) {
                  Text(
                    text = "${"%,d".format(state.liveTotalVolumeKg)} ",
                    color = StreakOnSurface,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "kg",
                    color = StreakOnSurfaceVariant,
                    fontSize = 11.sp
                  )
                }
              }
            }
          }

          LiquidGlassCard(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(20.dp)
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(CircleShape)
                  .background(Color(0x339DDF2E)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = StreakSecondary,
                  modifier = Modifier.size(19.dp)
                )
              }
              Column {
                Text(
                  text = "Progress",
                  color = StreakOnSurfaceVariant,
                  fontSize = 11.sp
                )
                Row(verticalAlignment = Alignment.Bottom) {
                  Text(
                    text = "$completedSets ",
                    color = StreakOnSurface,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "/ $totalSets Sets",
                    color = StreakOnSurfaceVariant,
                    fontSize = 11.sp
                  )
                }
              }
            }
          }
        }

        // Exercise Feed
        state.liveExercises.forEach { exercise ->
          ExerciseLogGlassCard(
            exercise = exercise,
            onAdjustWeight = onAdjustWeight,
            onAdjustReps = onAdjustReps,
            onLogSet = onLogSet,
            onAddSet = onAddSet
          )
        }

        // Add Exercise from Library Button in Liquid Glass
        LiquidGlassCard(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("add_exercise_library_button"),
          shape = RoundedCornerShape(22.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable(onClick = onAddExercise)
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(StreakPrimary.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = StreakPrimary,
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Add Exercise from Library (150+)",
              color = StreakPrimary,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Spacer(modifier = Modifier.height(100.dp))
      }

      // Bottom Floating Liquid Glass Action Bar
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(GlassNavBarBackground)
          .border(
            width = 1.dp,
            brush = GlassBorderGradient,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
          )
          .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
          .navigationBarsPadding()
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Mini Rest Indicator Pill
          LiquidGlassPill {
            Row(
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = null,
                tint = StreakPrimary,
                modifier = Modifier.size(16.dp)
              )
              Text(
                text = restStr,
                color = StreakPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          // Primary Finish Workout Action
          Box(
            modifier = Modifier
              .weight(1f)
              .height(50.dp)
              .clip(CircleShape)
              .background(
                Brush.horizontalGradient(
                  listOf(StreakPrimary, StreakSecondary)
                )
              )
              .border(1.dp, GlassBorderGradient, CircleShape)
              .clickable(onClick = onOpenFinishModal)
              .testTag("finish_workout_button"),
            contentAlignment = Alignment.Center
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Flag,
                contentDescription = null,
                tint = StreakOnPrimary,
                modifier = Modifier.size(18.dp)
              )
              Text(
                text = "Finish Workout",
                color = StreakOnPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    // Finish Workout Delight Modal in Liquid Glass
    if (state.showFinishWorkoutModal) {
      AlertDialog(
        onDismissRequest = onCloseFinishModal,
        containerColor = Color(0xF0101C33),
        shape = RoundedCornerShape(32.dp),
        title = {
          Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color(0x339DDF2E))
                .border(1.5.dp, GlassBorderActiveGradient, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.WorkspacePremium,
                contentDescription = null,
                tint = StreakSecondary,
                modifier = Modifier.size(34.dp)
              )
            }
            Text(
              text = "Crushed It! 🔥",
              color = StreakOnSurface,
              fontSize = 24.sp,
              fontWeight = FontWeight.ExtraBold
            )
            Text(
              text = "Session #32 completed with 1 new personal record registered.",
              color = StreakOnSurfaceVariant,
              fontSize = 13.sp,
              textAlign = TextAlign.Center
            )
          }
        },
        text = {
          LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = Color(0x4D0C1527)
          ) {
            Column(
              modifier = Modifier.padding(14.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(text = "Active Duration", color = StreakOnSurfaceVariant, fontSize = 12.sp)
                Text(text = "24m 42s", color = StreakOnSurface, fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(text = "Total Tonnage", color = StreakOnSurfaceVariant, fontSize = 12.sp)
                Text(text = "5,240 kg", color = StreakOnSurface, fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(text = "Volume Progression", color = StreakOnSurfaceVariant, fontSize = 12.sp)
                Text(text = "+4.2% vs last week", color = StreakPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        },
        confirmButton = {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .clip(CircleShape)
              .background(
                Brush.horizontalGradient(
                  listOf(StreakPrimary, StreakSecondary)
                )
              )
              .border(1.dp, GlassBorderGradient, CircleShape)
              .clickable(onClick = onCompleteWorkout)
              .testTag("save_and_review_summary_button"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              "Save & Review Summary",
              color = StreakOnPrimary,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }
        },
        dismissButton = {
          TextButton(
            onClick = onCloseFinishModal,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Resume Session", color = StreakOnSurfaceVariant)
          }
        }
      )
    }
  }
}

@Composable
private fun ExerciseLogGlassCard(
  exercise: WorkoutExercise,
  onAdjustWeight: (String, Int, Double) -> Unit,
  onAdjustReps: (String, Int, Int) -> Unit,
  onLogSet: (String, Int) -> Unit,
  onAddSet: (String) -> Unit
) {
  LiquidGlassCard(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("exercise_card_${exercise.id}"),
    shape = RoundedCornerShape(26.dp)
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
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
              .size(42.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(
                if (exercise.isActive) Color(0x334EDEA3)
                else Color(0x22162544)
              )
              .border(1.dp, GlassBorderGradient, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = when (exercise.equipment) {
                "Barbell" -> Icons.Default.SportsGymnastics
                "Cable" -> Icons.Default.Hardware
                else -> Icons.Default.FitnessCenter
              },
              contentDescription = null,
              tint = if (exercise.isActive) StreakPrimary else StreakOnSurfaceVariant,
              modifier = Modifier.size(22.dp)
            )
          }

          Column {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Text(
                text = exercise.name,
                color = StreakOnSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
              )
              LiquidGlassPill(isActive = exercise.isActive) {
                Text(
                  text = exercise.equipment,
                  color = if (exercise.isActive) StreakSecondary else StreakOnSurfaceVariant,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                )
              }
            }
            Text(
              text = exercise.muscleGroup,
              color = StreakOnSurfaceVariant,
              fontSize = 11.sp
            )
          }
        }

        if (exercise.isActive) {
          LiquidGlassPill(isActive = true) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(6.dp)
                  .clip(CircleShape)
                  .background(StreakSecondary)
              )
              Text(
                text = "ACTIVE",
                color = StreakSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        } else {
          Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
            tint = StreakOnSurfaceVariant,
            modifier = Modifier.size(20.dp)
          )
        }
      }

      // Sets list
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        exercise.sets.forEachIndexed { index, set ->
          if (exercise.isActive && !set.isCompleted && index == 1) {
            ActiveSetTargetGlassRow(
              exerciseId = exercise.id,
              setIndex = index,
              weight = set.weightKg,
              reps = set.reps,
              onAdjustWeight = onAdjustWeight,
              onAdjustReps = onAdjustReps,
              onLogSet = onLogSet
            )
          } else if (set.isCompleted) {
            LiquidGlassCard(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(14.dp),
              backgroundColor = Color(0x2E0B1629)
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
                  horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                  Text(
                    text = "S${set.setNumber}",
                    color = StreakOnSurfaceVariant,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                  Text(
                    text = "${set.weightKg} kg × ${set.reps} reps",
                    color = StreakOnSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                  )
                  if (set.isPr) {
                    LiquidGlassPill(isActive = true) {
                      Text(
                        text = "🏆 ${set.prLabel ?: "PR"}",
                        color = StreakSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                      )
                    }
                  }
                }

                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  set.rpe?.let {
                    Text(
                      text = it,
                      color = StreakOnSurfaceVariant,
                      fontSize = 11.sp
                    )
                  }
                  Box(
                    modifier = Modifier
                      .size(24.dp)
                      .clip(CircleShape)
                      .background(StreakPrimary),
                    contentAlignment = Alignment.Center
                  ) {
                    Icon(
                      imageVector = Icons.Default.Check,
                      contentDescription = "Completed",
                      tint = StreakOnPrimary,
                      modifier = Modifier.size(15.dp)
                    )
                  }
                }
              }
            }
          } else {
            LiquidGlassCard(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(14.dp),
              backgroundColor = Color(0x1A0B1629)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 14.dp, vertical = 9.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                  Text(
                    text = "S${set.setNumber}",
                    color = StreakOnSurfaceVariant,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                  )
                  Text(
                    text = "${set.weightKg} kg × ${set.reps} reps" +
                      if (set.isPrefilled) " (Prefilled)" else "",
                    color = StreakOnSurfaceVariant,
                    fontSize = 12.sp
                  )
                }

                if (set.isPrefilled) {
                  Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = StreakOnSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                  )
                } else {
                  Text(
                    text = set.statusNote ?: "Pending",
                    color = StreakOutline,
                    fontSize = 11.sp
                  )
                }
              }
            }
          }
        }
      }

      // Add Set button for active exercise
      if (exercise.isActive) {
        LiquidGlassPill(
          onClick = { onAddSet(exercise.id) },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("add_set_button_${exercise.id}")
        ) {
          Row(
            modifier = Modifier
              .align(Alignment.Center)
              .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = null,
              tint = StreakOnSurfaceVariant,
              modifier = Modifier.size(16.dp)
            )
            Text(
              text = "Add Set",
              color = StreakOnSurfaceVariant,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}

@Composable
private fun ActiveSetTargetGlassRow(
  exerciseId: String,
  setIndex: Int,
  weight: Double,
  reps: Int,
  onAdjustWeight: (String, Int, Double) -> Unit,
  onAdjustReps: (String, Int, Int) -> Unit,
  onLogSet: (String, Int) -> Unit
) {
  val logInteraction = remember { MutableInteractionSource() }
  val isLogPressed by logInteraction.collectIsPressedAsState()
  val logScale by animateFloatAsState(if (isLogPressed) 0.94f else 1f, label = "log_btn_scale")

  LiquidGlassCard(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("active_set_target_row"),
    shape = RoundedCornerShape(20.dp),
    backgroundColor = Color(0x40162544),
    borderBrush = GlassBorderActiveGradient
  ) {
    Column(
      modifier = Modifier.padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Text(
            text = "S${setIndex + 1}",
            color = StreakPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Previous: 14 kg × 12 reps",
            color = StreakOnSurfaceVariant,
            fontSize = 11.sp
          )
        }
        LiquidGlassPill(isActive = true) {
          Text(
            text = "CURRENT TARGET",
            color = StreakPrimary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
          )
        }
      }

      // Steppers for Weight and Reps with spring bouncy feedback
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Weight Stepper
        LiquidGlassCard(
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(16.dp),
          backgroundColor = Color(0x330B1629)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            LiquidGlassPill(onClick = { onAdjustWeight(exerciseId, setIndex, -0.5) }) {
              Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = null,
                tint = StreakOnSurface,
                modifier = Modifier
                  .size(28.dp)
                  .padding(6.dp)
              )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(text = "$weight", color = StreakOnSurface, fontSize = 16.sp, fontWeight = FontWeight.Bold)
              Text(text = "kg", color = StreakOnSurfaceVariant, fontSize = 10.sp)
            }

            LiquidGlassPill(onClick = { onAdjustWeight(exerciseId, setIndex, 0.5) }) {
              Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = StreakOnSurface,
                modifier = Modifier
                  .size(28.dp)
                  .padding(6.dp)
              )
            }
          }
        }

        // Reps Stepper
        LiquidGlassCard(
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(16.dp),
          backgroundColor = Color(0x330B1629)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            LiquidGlassPill(onClick = { onAdjustReps(exerciseId, setIndex, -1) }) {
              Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = null,
                tint = StreakOnSurface,
                modifier = Modifier
                  .size(28.dp)
                  .padding(6.dp)
              )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(text = "$reps", color = StreakOnSurface, fontSize = 16.sp, fontWeight = FontWeight.Bold)
              Text(text = "reps", color = StreakOnSurfaceVariant, fontSize = 10.sp)
            }

            LiquidGlassPill(onClick = { onAdjustReps(exerciseId, setIndex, 1) }) {
              Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = StreakOnSurface,
                modifier = Modifier
                  .size(28.dp)
                  .padding(6.dp)
              )
            }
          }
        }
      }

      // Log Set kinetic button with liquid glass sheen
      Box(
        modifier = Modifier
          .scale(logScale)
          .fillMaxWidth()
          .height(48.dp)
          .clip(CircleShape)
          .background(
            Brush.horizontalGradient(
              listOf(StreakPrimary, StreakSecondary)
            )
          )
          .border(1.dp, GlassBorderGradient, CircleShape)
          .clickable(
            interactionSource = logInteraction,
            indication = null,
            onClick = { onLogSet(exerciseId, setIndex) }
          )
          .testTag("log_current_set_button"),
        contentAlignment = Alignment.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = StreakOnPrimary,
            modifier = Modifier.size(18.dp)
          )
          Text(
            text = "Log Set ${setIndex + 1}",
            color = StreakOnPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}
