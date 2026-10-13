package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DayStatus
import com.example.data.DayVolumeStat
import com.example.data.PersonalRecord
import com.example.data.StreakfitUiState
import com.example.ui.components.AmbientGlassBackdrop
import com.example.ui.components.LiquidGlassCard
import com.example.ui.components.LiquidGlassPill
import com.example.ui.theme.GlassBorderActiveGradient
import com.example.ui.theme.GlassBorderGradient
import com.example.ui.theme.StreakOnPrimary
import com.example.ui.theme.StreakOnPrimaryContainer
import com.example.ui.theme.StreakOnSurface
import com.example.ui.theme.StreakOnSurfaceVariant
import com.example.ui.theme.StreakOutline
import com.example.ui.theme.StreakPrimary
import com.example.ui.theme.StreakPrimaryContainer
import com.example.ui.theme.StreakSecondary
import com.example.ui.theme.StreakSecondaryContainer
import com.example.ui.theme.StreakTertiary
import com.example.ui.theme.StreakTertiaryContainer

@Composable
fun ProgressScreen(
  state: StreakfitUiState,
  onSelectTimeframe: (String) -> Unit,
  onExport: (String) -> Unit
) {
  val scrollState = rememberScrollState()

  // Animate volume bar emergence on screen entry
  var startChartAnimation by remember { mutableStateOf(false) }
  LaunchedEffect(Unit) {
    startChartAnimation = true
  }

  AmbientGlassBackdrop {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(horizontal = 16.dp, vertical = 12.dp)
        .testTag("progress_screen"),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Timeframe Selector Liquid Glass Tabs
      LiquidGlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        backgroundColor = Color(0x660A1325)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          listOf("7 Days", "30 Days", "90 Days", "Year").forEach { tf ->
            val isSelected = state.selectedTimeframe == tf
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(CircleShape)
                .background(
                  if (isSelected) StreakPrimary
                  else Color.Transparent
                )
                .then(
                  if (isSelected) Modifier.border(1.dp, Color(0x66FFFFFF), CircleShape)
                  else Modifier
                )
                .clickable { onSelectTimeframe(tf) }
                .padding(vertical = 8.dp)
                .testTag("timeframe_tab_$tf"),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = tf,
                color = if (isSelected) StreakOnPrimary else StreakOnSurfaceVariant,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
              )
            }
          }
        }
      }

      // 2. Momentum Engine Liquid Glass Card
      LiquidGlassCard(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("momentum_engine_card"),
        shape = RoundedCornerShape(28.dp)
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          // Streak numbers
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "MOMENTUM ENGINE",
                color = StreakPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.2.sp
              )
              Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Text(
                  text = "${state.streakDays}",
                  color = StreakOnSurface,
                  fontSize = 34.sp,
                  fontWeight = FontWeight.ExtraBold
                )
                Text(
                  text = "Day Streak",
                  color = StreakPrimary,
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(bottom = 4.dp)
                )
              }
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "Personal Best",
                color = StreakOnSurfaceVariant,
                fontSize = 11.sp
              )
              LiquidGlassPill(
                isActive = true,
                modifier = Modifier.padding(top = 4.dp)
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.MilitaryTech,
                    contentDescription = null,
                    tint = StreakSecondary,
                    modifier = Modifier.size(16.dp)
                  )
                  Text(
                    text = "${state.personalBestDays} Days",
                    color = StreakOnSurface,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }

          // 30-Day Activity Heatmap Grid in Liquid Glass
          LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = Color(0x330B1629)
          ) {
            Column(
              modifier = Modifier.padding(14.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "30-Day Consistency",
                  color = StreakOnSurfaceVariant,
                  fontSize = 12.sp
                )
                Text(
                  text = "87% Active",
                  color = StreakPrimary,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
              }

              // 3 rows x 10 columns
              Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                for (row in 0 until 3) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                  ) {
                    for (col in 0 until 10) {
                      val index = row * 10 + col
                      val day = state.heatmapDays.getOrNull(index)
                      HeatmapCellItem(
                        status = day?.status ?: DayStatus.LOGGED,
                        modifier = Modifier.weight(1f)
                      )
                    }
                  }
                }
              }

              // Legend
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 4.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp),
                  modifier = Modifier.padding(end = 12.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .size(9.dp)
                      .clip(RoundedCornerShape(3.dp))
                      .background(Color(0x33162544))
                      .border(0.5.dp, GlassBorderGradient, RoundedCornerShape(3.dp))
                  )
                  Text(text = "Missed", color = StreakOnSurfaceVariant, fontSize = 10.sp)
                }

                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp),
                  modifier = Modifier.padding(end = 12.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .size(9.dp)
                      .clip(RoundedCornerShape(3.dp))
                      .background(StreakTertiaryContainer)
                  )
                  Text(text = "Freeze", color = StreakOnSurfaceVariant, fontSize = 10.sp)
                }

                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .size(9.dp)
                      .clip(RoundedCornerShape(3.dp))
                      .background(StreakPrimary)
                  )
                  Text(text = "Logged", color = StreakOnSurfaceVariant, fontSize = 10.sp)
                }
              }
            }
          }

          // Streak Freeze Status Card
          LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            backgroundColor = Color(0x33101D33)
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              verticalAlignment = Alignment.Top
            ) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(Color(0x3319AEE8)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.AcUnit,
                  contentDescription = null,
                  tint = StreakTertiary,
                  modifier = Modifier.size(19.dp)
                )
              }

              Column(modifier = Modifier.weight(1f)) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Text(
                    text = "1 freeze remaining this week",
                    color = StreakTertiary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                  LiquidGlassPill(isActive = true) {
                    Text(
                      text = "Active",
                      color = StreakTertiary,
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                    )
                  }
                }
                Text(
                  text = "Pauses streak if you miss a day without losing progress. Auto-renews every Monday.",
                  color = StreakOnSurfaceVariant,
                  fontSize = 11.sp,
                  lineHeight = 15.sp,
                  modifier = Modifier.padding(top = 2.dp)
                )
              }
            }
          }
        }
      }

      // 3. Habit Correlation Insight Liquid Glass Card
      LiquidGlassCard(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("correlation_insight_card"),
        shape = RoundedCornerShape(28.dp)
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            LiquidGlassPill(isActive = true) {
              Row(
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Lightbulb,
                  contentDescription = null,
                  tint = StreakSecondary,
                  modifier = Modifier.size(14.dp)
                )
                Text(
                  text = "HABIT CORRELATION INSIGHT",
                  color = StreakSecondary,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Text(
              text = "98.4% Confidence",
              color = StreakPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Text(
            text = "Hydration Powers Output",
            color = StreakOnSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )

          Text(
            text = "On days you complete your Morning Hydration habit, your workout volume increases by +18% and session completion is 94% higher.",
            color = StreakOnSurfaceVariant,
            fontSize = 13.sp,
            lineHeight = 18.sp
          )

          // Fluid Visual Lift Comparison Bars
          val hydratedAnim by animateFloatAsState(
            targetValue = if (startChartAnimation) 0.88f else 0f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
            label = "hydrated_bar_anim"
          )
          val dehydratedAnim by animateFloatAsState(
            targetValue = if (startChartAnimation) 0.65f else 0f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
            label = "dehydrated_bar_anim"
          )

          LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            backgroundColor = Color(0x330B1629)
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(text = "Hydrated Days", color = StreakOnSurfaceVariant, fontSize = 11.sp)
                Row(verticalAlignment = Alignment.Bottom) {
                  Text(text = "6,840 ", color = StreakPrimary, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                  Text(text = "kg avg", color = StreakOnSurfaceVariant, fontSize = 10.sp, modifier = Modifier.padding(bottom = 2.dp))
                }
                Box(
                  modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(Color(0x33162544))
                    .border(0.5.dp, GlassBorderGradient, CircleShape)
                ) {
                  Box(
                    modifier = Modifier
                      .fillMaxWidth(hydratedAnim)
                      .height(8.dp)
                      .clip(CircleShape)
                      .background(
                        Brush.horizontalGradient(
                          listOf(StreakPrimary, StreakSecondary)
                        )
                      )
                  )
                }
              }

              Column(modifier = Modifier.weight(1f)) {
                Text(text = "Dehydrated Days", color = StreakOnSurfaceVariant, fontSize = 11.sp)
                Row(verticalAlignment = Alignment.Bottom) {
                  Text(text = "5,790 ", color = StreakOnSurface, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                  Text(text = "kg avg", color = StreakOnSurfaceVariant, fontSize = 10.sp, modifier = Modifier.padding(bottom = 2.dp))
                }
                Box(
                  modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(Color(0x33162544))
                    .border(0.5.dp, GlassBorderGradient, CircleShape)
                ) {
                  Box(
                    modifier = Modifier
                      .fillMaxWidth(dehydratedAnim)
                      .height(8.dp)
                      .clip(CircleShape)
                      .background(StreakOutline.copy(alpha = 0.6f))
                  )
                }
              }
            }
          }
        }
      }

      // 4. Weekly Workout Summary & Volume Bar Chart with Spring Rise
      LiquidGlassCard(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("weekly_workout_summary_card"),
        shape = RoundedCornerShape(28.dp)
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "WEEKLY OVERVIEW",
                color = StreakPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.2.sp
              )
              Text(
                text = "Workout Summary",
                color = StreakOnSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
              )
            }

            LiquidGlassPill(isActive = true) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                  contentDescription = null,
                  tint = StreakPrimary,
                  modifier = Modifier.size(13.dp)
                )
                Text(
                  text = "+12% vs last wk",
                  color = StreakPrimary,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }

          // Triple Metric Grid in Liquid Glass
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            LiquidGlassCard(
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(16.dp),
              backgroundColor = Color(0x330B1629)
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Text(text = "Volume", color = StreakOnSurfaceVariant, fontSize = 11.sp)
                Text(text = "24.6k", color = StreakOnSurface, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Text(text = "kg lifted", color = StreakPrimary, fontSize = 10.sp)
              }
            }

            LiquidGlassCard(
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(16.dp),
              backgroundColor = Color(0x330B1629)
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Text(text = "Workouts", color = StreakOnSurfaceVariant, fontSize = 11.sp)
                Text(text = "4 of 4", color = StreakSecondary, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Text(text = "Target Met", color = StreakSecondary, fontSize = 10.sp)
              }
            }

            LiquidGlassCard(
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(16.dp),
              backgroundColor = Color(0x330B1629)
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Text(text = "Active Time", color = StreakOnSurfaceVariant, fontSize = 11.sp)
                Text(text = "3h 45m", color = StreakOnSurface, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                Text(text = "High tempo", color = StreakOnSurfaceVariant, fontSize = 10.sp)
              }
            }
          }

          // Volume Bar Chart with Fluid Springs
          LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            backgroundColor = Color(0x330B1629)
          ) {
            Column(
              modifier = Modifier.padding(14.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = "Volume Lifted by Session", color = StreakOnSurfaceVariant, fontSize = 11.sp)
                Text(text = "Peak: 7,200 kg (Sat)", color = StreakPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }

              // Animated Bars
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(115.dp)
                  .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
              ) {
                state.dayVolumes.forEach { stat ->
                  val targetFraction = if (stat.isRest) 0.05f else (stat.volumeKg / 7600.0).toFloat().coerceIn(0.1f, 1f)
                  val animatedHeightFraction by animateFloatAsState(
                    targetValue = if (startChartAnimation) targetFraction else 0f,
                    animationSpec = spring(
                      dampingRatio = Spring.DampingRatioMediumBouncy,
                      stiffness = Spring.StiffnessLow
                    ),
                    label = "chart_bar_${stat.dayLabel}"
                  )

                  Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                  ) {
                    Text(
                      text = stat.displayStr,
                      color = if (stat.isPeak) StreakSecondary else StreakOnSurfaceVariant,
                      fontSize = 10.sp,
                      fontWeight = if (stat.isPeak) FontWeight.Bold else FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                      modifier = Modifier
                        .width(22.dp)
                        .height((90 * animatedHeightFraction).dp)
                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                        .background(
                          when {
                            stat.isPeak -> Brush.verticalGradient(
                              listOf(StreakSecondary, StreakSecondaryContainer)
                            )
                            stat.isRest -> Brush.verticalGradient(
                              listOf(Color(0x33162544), Color(0x11162544))
                            )
                            else -> Brush.verticalGradient(
                              listOf(StreakPrimary.copy(alpha = 0.7f), StreakPrimaryContainer.copy(alpha = 0.3f))
                            )
                          }
                        )
                        .border(
                          0.5.dp,
                          if (stat.isPeak) GlassBorderActiveGradient else GlassBorderGradient,
                          RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                      text = stat.dayLabel,
                      color = if (stat.isPeak) StreakSecondary else StreakOnSurfaceVariant,
                      fontSize = 11.sp,
                      fontWeight = if (stat.isPeak) FontWeight.Bold else FontWeight.Medium
                    )
                  }
                }
              }
            }
          }
        }
      }

      // 5. Trophy Shelf (Personal Records)
      LiquidGlassCard(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("trophy_shelf_card"),
        shape = RoundedCornerShape(28.dp)
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(30.dp)
                  .clip(CircleShape)
                  .background(Color(0x339DDF2E)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.WorkspacePremium,
                  contentDescription = null,
                  tint = StreakSecondary,
                  modifier = Modifier.size(18.dp)
                )
              }
              Text(
                text = "Trophy Shelf",
                color = StreakOnSurface,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
              Text(text = "All PRs", color = StreakPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
              Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = StreakPrimary, modifier = Modifier.size(14.dp))
            }
          }

          // PR list in Liquid Glass
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            state.personalRecords.forEach { pr ->
              PersonalRecordGlassItem(pr)
            }
          }
        }
      }

      // 6. Data Sovereignty & Privacy Section
      LiquidGlassCard(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("data_export_card"),
        shape = RoundedCornerShape(26.dp)
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
                text = "Data Sovereignty & Privacy",
                color = StreakOnSurface,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "All biometric and performance telemetry belongs to you.",
                color = StreakOnSurfaceVariant,
                fontSize = 11.sp
              )
            }
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = null,
              tint = StreakOutline,
              modifier = Modifier.size(18.dp)
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            LiquidGlassPill(
              onClick = { onExport("CSV") },
              modifier = Modifier
                .weight(1f)
                .testTag("export_csv_btn")
            ) {
              Row(
                modifier = Modifier
                  .align(Alignment.Center)
                  .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = StreakPrimary, modifier = Modifier.size(16.dp))
                Text(text = "Export CSV", color = StreakOnSurface, fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }

            LiquidGlassPill(
              onClick = { onExport("JSON") },
              modifier = Modifier
                .weight(1f)
                .testTag("export_json_btn")
            ) {
              Row(
                modifier = Modifier
                  .align(Alignment.Center)
                  .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(imageVector = Icons.Default.DataObject, contentDescription = null, tint = StreakTertiary, modifier = Modifier.size(16.dp))
                Text(text = "Export JSON", color = StreakOnSurface, fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(96.dp))
    }
  }
}

@Composable
private fun HeatmapCellItem(status: DayStatus, modifier: Modifier = Modifier) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse_today_cell")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.95f,
    targetValue = 1.05f,
    animationSpec = infiniteRepeatable(
      animation = tween(900, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_today_scale"
  )

  Box(
    modifier = modifier
      .then(if (status == DayStatus.TODAY_ACTIVE) Modifier.scale(pulseScale) else Modifier)
      .height(24.dp)
      .clip(RoundedCornerShape(5.dp))
      .background(
        when (status) {
          DayStatus.LOGGED -> StreakPrimary
          DayStatus.FREEZE -> StreakTertiaryContainer
          DayStatus.TODAY_ACTIVE -> StreakSecondary
          DayStatus.MISSED -> Color(0x2B162544)
        }
      )
      .border(
        0.5.dp,
        if (status == DayStatus.TODAY_ACTIVE) GlassBorderActiveGradient else GlassBorderGradient,
        RoundedCornerShape(5.dp)
      ),
    contentAlignment = Alignment.Center
  ) {
    when (status) {
      DayStatus.LOGGED -> {
        Text(text = "✓", color = StreakOnPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
      }
      DayStatus.FREEZE -> {
        Text(text = "❄", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
      }
      DayStatus.TODAY_ACTIVE -> {
        Text(text = "⚡", color = StreakOnPrimaryContainer, fontSize = 10.sp, fontWeight = FontWeight.Bold)
      }
      DayStatus.MISSED -> {}
    }
  }
}

@Composable
private fun PersonalRecordGlassItem(pr: PersonalRecord) {
  LiquidGlassCard(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("pr_item_${pr.id}"),
    shape = RoundedCornerShape(16.dp),
    backgroundColor = Color(0x330B1629)
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
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(
              if (pr.badge != null) Color(0x339DDF2E)
              else Color(0x334EDEA3)
            )
            .border(1.dp, GlassBorderGradient, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = when (pr.iconType) {
              "fitness_center" -> Icons.Default.FitnessCenter
              "sports_martial_arts" -> Icons.Default.SportsMartialArts
              else -> Icons.Default.Timer
            },
            contentDescription = null,
            tint = if (pr.badge != null) StreakSecondary else StreakPrimary,
            modifier = Modifier.size(20.dp)
          )
        }

        Column {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text(text = pr.exerciseName, color = StreakOnSurface, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            if (pr.badge != null) {
              LiquidGlassPill(isActive = true) {
                Text(
                  text = pr.badge,
                  color = StreakSecondary,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                )
              }
            }
          }
          Text(text = pr.detail, color = StreakOnSurfaceVariant, fontSize = 11.sp)
        }
      }

      Column(horizontalAlignment = Alignment.End) {
        Row(verticalAlignment = Alignment.Bottom) {
          Text(
            text = "${pr.weightKg}",
            color = if (pr.badge != null) StreakSecondary else StreakOnSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
          )
          Text(text = " kg", color = StreakOnSurfaceVariant, fontSize = 11.sp, modifier = Modifier.padding(bottom = 2.dp))
        }
        pr.bump?.let {
          Text(
            text = it,
            color = if (pr.badge != null) StreakPrimary else StreakOnSurfaceVariant,
            fontSize = 11.sp,
            fontWeight = if (pr.badge != null) FontWeight.Bold else FontWeight.Normal
          )
        }
      }
    }
  }
}
