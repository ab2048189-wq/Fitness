package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.ActiveScreen
import com.example.data.StreakfitTab
import com.example.data.StreakfitViewModel
import com.example.ui.components.AmbientGlassBackdrop
import com.example.ui.components.LiquidGlassCard
import com.example.ui.components.StreakfitBottomNav
import com.example.ui.components.StreakfitTopBar
import com.example.ui.screens.ActiveWorkoutScreen
import com.example.ui.screens.HabitsListScreen
import com.example.ui.screens.NewHabitScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.TodayScreen
import com.example.ui.screens.WorkoutsListScreen
import com.example.ui.theme.GlassBorderActiveGradient
import com.example.ui.theme.GlassBorderGradient
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.StreakBackground
import com.example.ui.theme.StreakOnSurface
import com.example.ui.theme.StreakPrimary
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        StreakfitApp()
      }
    }
  }
}

@Composable
fun StreakfitApp(viewModel: StreakfitViewModel = viewModel()) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  // Auto-clear toast
  LaunchedEffect(uiState.toastMessage) {
    if (uiState.toastMessage != null) {
      delay(2600)
      viewModel.clearToast()
    }
  }

  // Handle hardware back
  BackHandler(enabled = uiState.currentScreen != ActiveScreen.MAIN_TABS || uiState.selectedTab != StreakfitTab.TODAY) {
    if (uiState.currentScreen != ActiveScreen.MAIN_TABS) {
      viewModel.navigateTo(ActiveScreen.MAIN_TABS)
    } else {
      viewModel.setTab(StreakfitTab.TODAY)
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(StreakBackground)
  ) {
    AnimatedContent(
      targetState = uiState.currentScreen,
      transitionSpec = {
        if (targetState == ActiveScreen.ACTIVE_WORKOUT || targetState == ActiveScreen.NEW_HABIT) {
          (slideInVertically(
            initialOffsetY = { it },
            animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMedium)
          ) + fadeIn()).togetherWith(
            slideOutVertically(targetOffsetY = { -it / 4 }) + fadeOut()
          )
        } else {
          (slideInVertically(
            initialOffsetY = { -it / 4 },
            animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMedium)
          ) + fadeIn()).togetherWith(
            slideOutVertically(targetOffsetY = { it }) + fadeOut()
          )
        }
      },
      label = "screen_transition"
    ) { screen ->
      when (screen) {
        ActiveScreen.ACTIVE_WORKOUT -> {
          ActiveWorkoutScreen(
            state = uiState,
            onBack = { viewModel.navigateTo(ActiveScreen.MAIN_TABS) },
            onAddRestTime = { viewModel.addRestTime(it) },
            onSkipRestTime = { viewModel.skipRestTime() },
            onAdjustWeight = { exId, idx, delta -> viewModel.adjustSetWeight(exId, idx, delta) },
            onAdjustReps = { exId, idx, delta -> viewModel.adjustSetReps(exId, idx, delta) },
            onLogSet = { exId, idx -> viewModel.logSet(exId, idx) },
            onAddSet = { exId -> viewModel.addSetToExercise(exId) },
            onAddExercise = { viewModel.addExerciseFromLibrary() },
            onOpenFinishModal = { viewModel.openFinishModal() },
            onCloseFinishModal = { viewModel.closeFinishModal() },
            onCompleteWorkout = { viewModel.completeWorkoutSession() }
          )
        }

        ActiveScreen.NEW_HABIT -> {
          NewHabitScreen(
            streakDays = uiState.streakDays,
            onBack = { viewModel.navigateTo(ActiveScreen.MAIN_TABS) },
            onSaveHabit = { title, icon, color, type, target, unit, time, forgiving ->
              viewModel.addNewHabit(title, icon, color, type, target, unit, time, forgiving)
            }
          )
        }

        ActiveScreen.MAIN_TABS -> {
          Scaffold(
            topBar = {
              StreakfitTopBar(
                title = when (uiState.selectedTab) {
                  StreakfitTab.TODAY -> "Today"
                  StreakfitTab.WORKOUTS -> "Workouts"
                  StreakfitTab.HABITS -> "Habits"
                  StreakfitTab.PROGRESS -> "Progress"
                },
                streakDays = uiState.streakDays
              )
            },
            bottomBar = {
              StreakfitBottomNav(
                currentTab = uiState.selectedTab,
                onTabSelected = { viewModel.setTab(it) }
              )
            },
            containerColor = Color.Transparent
          ) { paddingValues ->
            Box(
              modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
            ) {
              AnimatedContent(
                targetState = uiState.selectedTab,
                transitionSpec = {
                  (fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) +
                    slideInHorizontally(
                      initialOffsetX = { 40 },
                      animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow)
                    )).togetherWith(
                    fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)) +
                      slideOutHorizontally(
                        targetOffsetX = { -40 },
                        animationSpec = spring(dampingRatio = 0.85f, stiffness = Spring.StiffnessMediumLow)
                      )
                  )
                },
                label = "tab_transition"
              ) { tab ->
                when (tab) {
                  StreakfitTab.TODAY -> {
                    TodayScreen(
                      state = uiState,
                      onStartWorkout = { viewModel.navigateTo(ActiveScreen.ACTIVE_WORKOUT) },
                      onAddHabit = { viewModel.navigateTo(ActiveScreen.NEW_HABIT) },
                      onToggleHabit = { viewModel.toggleHabit(it) },
                      onAdjustWater = { viewModel.adjustWater(it) },
                      onCompleteWater = { viewModel.completeWaterGoal() }
                    )
                  }

                  StreakfitTab.WORKOUTS -> {
                    WorkoutsListScreen(
                      state = uiState,
                      onStartSession = { viewModel.navigateTo(ActiveScreen.ACTIVE_WORKOUT) }
                    )
                  }

                  StreakfitTab.HABITS -> {
                    HabitsListScreen(
                      state = uiState,
                      onAddNewHabit = { viewModel.navigateTo(ActiveScreen.NEW_HABIT) },
                      onToggleHabit = { viewModel.toggleHabit(it) }
                    )
                  }

                  StreakfitTab.PROGRESS -> {
                    ProgressScreen(
                      state = uiState,
                      onSelectTimeframe = { viewModel.setTimeframe(it) },
                      onExport = { viewModel.exportData(it) }
                    )
                  }
                }
              }
            }
          }
        }
      }
    }

    // Floating iOS Liquid Glass Toast Alert
    AnimatedVisibility(
      visible = uiState.toastMessage != null,
      enter = slideInVertically(
        initialOffsetY = { it },
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
      ) + fadeIn(),
      exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 100.dp)
    ) {
      uiState.toastMessage?.let { msg ->
        LiquidGlassCard(
          shape = CircleShape,
          backgroundColor = Color(0xD9101C33),
          borderBrush = GlassBorderActiveGradient,
          modifier = Modifier
            .padding(horizontal = 24.dp)
            .testTag("streakfit_toast_bubble")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = StreakPrimary,
              modifier = Modifier.size(19.dp)
            )
            Text(
              text = msg,
              color = StreakOnSurface,
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }
  }
}
