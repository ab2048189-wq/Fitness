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
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StreakfitUiState
import com.example.ui.components.AmbientGlassBackdrop
import com.example.ui.components.LiquidGlassCard
import com.example.ui.components.LiquidGlassPill
import com.example.ui.theme.GlassBorderGradient
import com.example.ui.theme.StreakBackground
import com.example.ui.theme.StreakOnPrimaryContainer
import com.example.ui.theme.StreakOnSurface
import com.example.ui.theme.StreakOnSurfaceVariant
import com.example.ui.theme.StreakPrimary
import com.example.ui.theme.StreakPrimaryContainer

data class WorkoutRoutineTemplate(
  val title: String,
  val split: String,
  val exercisesCount: Int,
  val estimatedMins: Int,
  val volumeKg: Int,
  val muscles: List<String>
)

@Composable
fun WorkoutsListScreen(
  state: StreakfitUiState,
  onStartSession: () -> Unit
) {
  val routines = listOf(
    WorkoutRoutineTemplate(
      title = "Upper Body Hypertrophy",
      split = "Day 1 · Push & Delts",
      exercisesCount = 5,
      estimatedMins = 45,
      volumeKg = 8400,
      muscles = listOf("Chest", "Shoulders", "Triceps")
    ),
    WorkoutRoutineTemplate(
      title = "Lower Body Quad Focus",
      split = "Day 2 · Legs & Core",
      exercisesCount = 6,
      estimatedMins = 55,
      volumeKg = 9200,
      muscles = listOf("Quads", "Hamstrings", "Calves")
    ),
    WorkoutRoutineTemplate(
      title = "Back & Posterior Chain",
      split = "Day 3 · Pull",
      exercisesCount = 5,
      estimatedMins = 48,
      volumeKg = 8800,
      muscles = listOf("Lats", "Upper Back", "Biceps")
    ),
    WorkoutRoutineTemplate(
      title = "Shoulder & Arm Finisher",
      split = "Day 4 · Accessories",
      exercisesCount = 4,
      estimatedMins = 35,
      volumeKg = 5100,
      muscles = listOf("Deltoids", "Biceps", "Forearms")
    )
  )

  val scrollState = rememberScrollState()

  AmbientGlassBackdrop {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(horizontal = 16.dp, vertical = 12.dp)
        .testTag("workouts_list_screen"),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Top banner in Liquid Glass
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
              text = "TRAINING SPLIT",
              color = StreakPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 1.2.sp
            )
            Text(
              text = "Hypertrophy Program",
              color = StreakOnSurface,
              fontSize = 19.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Week 6 of 8 · Progressive Overload",
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
              imageVector = Icons.Default.FitnessCenter,
              contentDescription = null,
              tint = StreakPrimary,
              modifier = Modifier.size(24.dp)
            )
          }
        }
      }

      // Active Routine Header
      Text(
        text = "Routines & Sessions",
        color = StreakOnSurface,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold
      )

      // Routines cards
      routines.forEachIndexed { index, routine ->
        LiquidGlassCard(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("routine_card_$index"),
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
              Column {
                Text(
                  text = routine.title,
                  color = StreakOnSurface,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = routine.split,
                  color = StreakPrimary,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Medium
                )
              }

              LiquidGlassPill {
                Text(
                  text = "Est. ${routine.estimatedMins}m",
                  color = StreakOnSurfaceVariant,
                  fontSize = 11.sp,
                  modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                )
              }
            }

            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              routine.muscles.forEach { muscle ->
                LiquidGlassPill {
                  Text(
                    text = muscle,
                    color = StreakOnSurfaceVariant,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                  )
                }
              }
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "${routine.exercisesCount} Exercises • ${"%,d".format(routine.volumeKg)} kg Volume",
                color = StreakOnSurfaceVariant,
                fontSize = 12.sp
              )

              Box(
                modifier = Modifier
                  .height(38.dp)
                  .clip(CircleShape)
                  .background(
                    Brush.horizontalGradient(
                      listOf(StreakPrimaryContainer, StreakPrimary)
                    )
                  )
                  .border(1.dp, GlassBorderGradient, CircleShape)
                  .clickable(onClick = onStartSession)
                  .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = StreakOnPrimaryContainer,
                    modifier = Modifier.size(16.dp)
                  )
                  Text("Start", color = StreakOnPrimaryContainer, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
