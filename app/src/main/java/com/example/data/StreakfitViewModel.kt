package com.example.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class StreakfitUiState(
  val currentScreen: ActiveScreen = ActiveScreen.MAIN_TABS,
  val selectedTab: StreakfitTab = StreakfitTab.TODAY,
  val selectedTimeframe: String = "30 Days",
  val streakDays: Int = 14,
  val personalBestDays: Int = 28,
  val freeRemainingThisWeek: Int = 1,
  val habits: List<Habit> = emptyList(),
  val weekCalendar: List<WeekCalendarDay> = emptyList(),
  val heatmapDays: List<HeatmapDay> = emptyList(),
  val dayVolumes: List<DayVolumeStat> = emptyList(),
  val personalRecords: List<PersonalRecord> = emptyList(),
  // Live workout state
  val liveWorkoutTitle: String = "Upper Body Hypertrophy",
  val liveWorkoutSubtitle: String = "(#32)",
  val elapsedSeconds: Int = 24 * 60 + 18,
  val restSeconds: Int = 72,
  val maxRestSeconds: Int = 90,
  val isRestTimerRunning: Boolean = true,
  val liveExercises: List<WorkoutExercise> = emptyList(),
  val liveTotalVolumeKg: Int = 5240,
  val liveTargetVolumeKg: Int = 8400,
  val showFinishWorkoutModal: Boolean = false,
  // Toast notifications
  val toastMessage: String? = null
)

class StreakfitViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(StreakfitUiState())
  val uiState: StateFlow<StreakfitUiState> = _uiState.asStateFlow()

  init {
    loadInitialData()
    startTimers()
  }

  private fun loadInitialData() {
    val weekCalendar = listOf(
      WeekCalendarDay("M", 14, isCompleted = true, isToday = false),
      WeekCalendarDay("T", 15, isCompleted = true, isToday = false),
      WeekCalendarDay("W", 16, isCompleted = true, isToday = false),
      WeekCalendarDay("T", 17, isCompleted = true, isToday = false),
      WeekCalendarDay("F", 18, isCompleted = false, isToday = true),
      WeekCalendarDay("S", 19, isCompleted = false, isToday = false),
      WeekCalendarDay("S", 20, isCompleted = false, isToday = false)
    )

    val habits = listOf(
      Habit(
        id = "h1",
        title = "Drink 2.5L Water",
        icon = "💧",
        streakDays = 12,
        metricType = HabitMetricType.COUNT,
        currentValue = 2000,
        targetValue = 2500,
        unit = "ml",
        isCompleted = false,
        isWaterStepper = true,
        colorTone = "cyan"
      ),
      Habit(
        id = "h2",
        title = "Morning Mobility & Stretch",
        icon = "🧘",
        streakDays = 28,
        metricType = HabitMetricType.CHECK_IN,
        currentValue = 1,
        targetValue = 1,
        isCompleted = true,
        subtitle = "Completed at 7:15 AM (15 mins)",
        colorTone = "emerald"
      ),
      Habit(
        id = "h3",
        title = "10,000 Daily Steps",
        icon = "🏃",
        streakDays = 14,
        metricType = HabitMetricType.COUNT,
        currentValue = 8420,
        targetValue = 10000,
        unit = "steps",
        isCompleted = false,
        isHealthConnectSynced = true,
        colorTone = "amber"
      ),
      Habit(
        id = "h4",
        title = "Creatine & Protein Shake",
        icon = "💊",
        streakDays = 41,
        metricType = HabitMetricType.CHECK_IN,
        currentValue = 1,
        targetValue = 1,
        isCompleted = true,
        subtitle = "5g Creatine + 30g Whey",
        colorTone = "emerald"
      ),
      Habit(
        id = "h5",
        title = "Read 20 Mins (Evening)",
        icon = "📖",
        streakDays = 9,
        metricType = HabitMetricType.DURATION,
        currentValue = 0,
        targetValue = 20,
        unit = "mins",
        isCompleted = false,
        subtitle = "Reminder scheduled at 9:00 PM",
        colorTone = "slate"
      )
    )

    // 30 days heatmap matching mockup
    // 30 items:
    // W1: ✓, ✓, Missed, ✓, ✓, ✓, ✓, Missed, ✓, ✓ (10)
    // W2-3: ✓, ✓, ✓, Freeze ❄, ✓, ✓, ✓, ✓, Missed, ✓ (10)
    // W3-4: ✓, ✓, ✓, ✓, ✓, ✓, ✓, ✓, ✓, Today ⚡ (10)
    val heatmap = mutableListOf<HeatmapDay>()
    val pattern = listOf(
      DayStatus.LOGGED, DayStatus.LOGGED, DayStatus.MISSED, DayStatus.LOGGED, DayStatus.LOGGED,
      DayStatus.LOGGED, DayStatus.LOGGED, DayStatus.MISSED, DayStatus.LOGGED, DayStatus.LOGGED,
      DayStatus.LOGGED, DayStatus.LOGGED, DayStatus.LOGGED, DayStatus.FREEZE, DayStatus.LOGGED,
      DayStatus.LOGGED, DayStatus.LOGGED, DayStatus.LOGGED, DayStatus.MISSED, DayStatus.LOGGED,
      DayStatus.LOGGED, DayStatus.LOGGED, DayStatus.LOGGED, DayStatus.LOGGED, DayStatus.LOGGED,
      DayStatus.LOGGED, DayStatus.LOGGED, DayStatus.LOGGED, DayStatus.LOGGED, DayStatus.TODAY_ACTIVE
    )
    pattern.forEachIndexed { index, status ->
      heatmap.add(HeatmapDay(index + 1, status))
    }

    val dayVolumes = listOf(
      DayVolumeStat("Mon", 5400.0, "5.4k", isRest = false),
      DayVolumeStat("Tue", 0.0, "—", isRest = true),
      DayVolumeStat("Wed", 6100.0, "6.1k", isRest = false),
      DayVolumeStat("Thu", 0.0, "—", isRest = true),
      DayVolumeStat("Fri", 5900.0, "5.9k", isRest = false),
      DayVolumeStat("Sat", 7200.0, "7.2k", isRest = false, isPeak = true),
      DayVolumeStat("Sun", 0.0, "—", isRest = true)
    )

    val personalRecords = listOf(
      PersonalRecord(
        id = "pr1",
        exerciseName = "Bench Press",
        weightKg = 105,
        detail = "2 days ago · Chest & Tri",
        badge = "New PR",
        bump = "+5kg bump",
        iconType = "fitness_center"
      ),
      PersonalRecord(
        id = "pr2",
        exerciseName = "Barbell Squat",
        weightKg = 140,
        detail = "5 reps logged · 12 days ago",
        badge = null,
        bump = "1RM est: 157kg",
        iconType = "sports_martial_arts"
      ),
      PersonalRecord(
        id = "pr3",
        exerciseName = "Deadlift",
        weightKg = 165,
        detail = "Conventional stance · 3 wks ago",
        badge = null,
        bump = "Solid lock",
        iconType = "timer"
      )
    )

    val exercises = listOf(
      WorkoutExercise(
        id = "e1",
        name = "Incline Bench Press",
        equipment = "Barbell",
        muscleGroup = "Chest · Shoulders",
        isActive = false,
        sets = listOf(
          ExerciseSet("s1_1", 1, 80.0, 10, rpe = "RPE 8", isCompleted = true),
          ExerciseSet("s1_2", 2, 82.5, 8, rpe = "RPE 9", isCompleted = true, isPr = true, prLabel = "PR 8RM"),
          ExerciseSet("s1_3", 3, 82.5, 7, rpe = "RPE 9.5", isCompleted = true)
        )
      ),
      WorkoutExercise(
        id = "e2",
        name = "Lateral Raises",
        equipment = "Dumbbells",
        muscleGroup = "Lateral Delts · FR-7 Prefill",
        isActive = true,
        sets = listOf(
          ExerciseSet("s2_1", 1, 14.0, 12, isCompleted = true),
          ExerciseSet("s2_2", 2, 14.0, 12, isCompleted = false),
          ExerciseSet("s2_3", 3, 14.0, 10, isCompleted = false, isPrefilled = true)
        )
      ),
      WorkoutExercise(
        id = "e3",
        name = "Tricep Pushdown",
        equipment = "Cable",
        muscleGroup = "3 sets preloaded from last week",
        isActive = false,
        sets = listOf(
          ExerciseSet("s3_1", 1, 32.5, 12, statusNote = "Pending"),
          ExerciseSet("s3_2", 2, 35.0, 10, statusNote = "Pending"),
          ExerciseSet("s3_3", 3, 35.0, 8, statusNote = "Pending")
        )
      )
    )

    _uiState.update {
      it.copy(
        weekCalendar = weekCalendar,
        habits = habits,
        heatmapDays = heatmap,
        dayVolumes = dayVolumes,
        personalRecords = personalRecords,
        liveExercises = exercises
      )
    }
  }

  private fun startTimers() {
    viewModelScope.launch {
      while (isActive) {
        delay(1000)
        _uiState.update { state ->
          val newElapsed = if (state.currentScreen == ActiveScreen.ACTIVE_WORKOUT) {
            state.elapsedSeconds + 1
          } else {
            state.elapsedSeconds
          }

          val newRest = if (state.isRestTimerRunning && state.restSeconds > 0) {
            state.restSeconds - 1
          } else {
            state.restSeconds
          }

          state.copy(
            elapsedSeconds = newElapsed,
            restSeconds = newRest
          )
        }
      }
    }
  }

  fun setTab(tab: StreakfitTab) {
    _uiState.update { it.copy(selectedTab = tab, currentScreen = ActiveScreen.MAIN_TABS) }
  }

  fun navigateTo(screen: ActiveScreen) {
    _uiState.update { it.copy(currentScreen = screen) }
  }

  fun setTimeframe(tf: String) {
    _uiState.update { it.copy(selectedTimeframe = tf) }
  }

  fun adjustWater(delta: Int) {
    _uiState.update { state ->
      val updated = state.habits.map { habit ->
        if (habit.isWaterStepper) {
          val newVal = (habit.currentValue + delta).coerceIn(0, 3500)
          habit.copy(
            currentValue = newVal,
            isCompleted = newVal >= habit.targetValue
          )
        } else habit
      }
      state.copy(habits = updated)
    }
  }

  fun completeWaterGoal() {
    _uiState.update { state ->
      val updated = state.habits.map { habit ->
        if (habit.isWaterStepper) {
          habit.copy(currentValue = habit.targetValue, isCompleted = true)
        } else habit
      }
      state.copy(habits = updated)
    }
  }

  fun toggleHabit(habitId: String) {
    _uiState.update { state ->
      val updated = state.habits.map { habit ->
        if (habit.id == habitId) {
          val completed = !habit.isCompleted
          habit.copy(
            isCompleted = completed,
            currentValue = if (completed) habit.targetValue else 0
          )
        } else habit
      }
      state.copy(habits = updated)
    }
  }

  fun addRestTime(seconds: Int = 30) {
    _uiState.update { it.copy(restSeconds = it.restSeconds + seconds, isRestTimerRunning = true) }
  }

  fun skipRestTime() {
    _uiState.update { it.copy(restSeconds = 0) }
  }

  fun adjustSetWeight(exerciseId: String, setIndex: Int, delta: Double) {
    _uiState.update { state ->
      val exercises = state.liveExercises.map { ex ->
        if (ex.id == exerciseId) {
          val sets = ex.sets.mapIndexed { idx, s ->
            if (idx == setIndex) {
              val newWeight = (s.weightKg + delta).coerceAtLeast(0.0)
              s.copy(weightKg = (newWeight * 10).toInt() / 10.0)
            } else s
          }
          ex.copy(sets = sets)
        } else ex
      }
      state.copy(liveExercises = exercises)
    }
  }

  fun adjustSetReps(exerciseId: String, setIndex: Int, delta: Int) {
    _uiState.update { state ->
      val exercises = state.liveExercises.map { ex ->
        if (ex.id == exerciseId) {
          val sets = ex.sets.mapIndexed { idx, s ->
            if (idx == setIndex) {
              val newReps = (s.reps + delta).coerceAtLeast(1)
              s.copy(reps = newReps)
            } else s
          }
          ex.copy(sets = sets)
        } else ex
      }
      state.copy(liveExercises = exercises)
    }
  }

  fun logSet(exerciseId: String, setIndex: Int) {
    _uiState.update { state ->
      var addedVolume = 0
      val exercises = state.liveExercises.map { ex ->
        if (ex.id == exerciseId) {
          val sets = ex.sets.mapIndexed { idx, s ->
            if (idx == setIndex) {
              addedVolume = (s.weightKg * s.reps).toInt()
              s.copy(isCompleted = true)
            } else s
          }
          ex.copy(sets = sets)
        } else ex
      }
      state.copy(
        liveExercises = exercises,
        liveTotalVolumeKg = state.liveTotalVolumeKg + addedVolume,
        restSeconds = 90,
        isRestTimerRunning = true
      )
    }
    showToast("Set logged! Rest timer started.")
  }

  fun addSetToExercise(exerciseId: String) {
    _uiState.update { state ->
      val exercises = state.liveExercises.map { ex ->
        if (ex.id == exerciseId) {
          val lastSet = ex.sets.lastOrNull()
          val newSetNumber = (lastSet?.setNumber ?: 0) + 1
          val newSet = ExerciseSet(
            id = "${exerciseId}_s$newSetNumber",
            setNumber = newSetNumber,
            weightKg = lastSet?.weightKg ?: 14.0,
            reps = lastSet?.reps ?: 10,
            isCompleted = false
          )
          ex.copy(sets = ex.sets + newSet)
        } else ex
      }
      state.copy(liveExercises = exercises)
    }
  }

  fun addExerciseFromLibrary() {
    val newEx = WorkoutExercise(
      id = "e_${System.currentTimeMillis()}",
      name = "Face Pulls",
      equipment = "Cable",
      muscleGroup = "Rear Delts · Upper Back",
      isActive = false,
      sets = listOf(
        ExerciseSet("fp_1", 1, 20.0, 15, isCompleted = false),
        ExerciseSet("fp_2", 2, 22.5, 12, isCompleted = false)
      )
    )
    _uiState.update { it.copy(liveExercises = it.liveExercises + newEx) }
    showToast("Added Face Pulls to workout session")
  }

  fun openFinishModal() {
    _uiState.update { it.copy(showFinishWorkoutModal = true) }
  }

  fun closeFinishModal() {
    _uiState.update { it.copy(showFinishWorkoutModal = false) }
  }

  fun completeWorkoutSession() {
    _uiState.update {
      it.copy(
        showFinishWorkoutModal = false,
        currentScreen = ActiveScreen.MAIN_TABS,
        selectedTab = StreakfitTab.TODAY
      )
    }
    showToast("Workout finished! 5,240 kg logged to your streak record 🔥")
  }

  fun addNewHabit(
    title: String,
    icon: String,
    colorTone: String,
    metricType: HabitMetricType,
    targetValue: Int,
    unit: String,
    reminderTime: String,
    forgivingStreak: Boolean
  ) {
    val newHabit = Habit(
      id = "h_${System.currentTimeMillis()}",
      title = title.ifBlank { "New Routine" },
      icon = icon,
      streakDays = 1,
      metricType = metricType,
      currentValue = 0,
      targetValue = targetValue,
      unit = unit,
      isCompleted = false,
      subtitle = "Reminder scheduled at $reminderTime",
      colorTone = colorTone,
      reminderTime = reminderTime,
      reminderEnabled = true,
      forgivingStreak = forgivingStreak
    )
    _uiState.update { state ->
      state.copy(
        habits = state.habits + newHabit,
        currentScreen = ActiveScreen.MAIN_TABS,
        selectedTab = StreakfitTab.TODAY
      )
    }
    showToast("Habit '$title' added! Streak #1 ready.")
  }

  fun exportData(format: String) {
    showToast("$format export generated & saved to downloads!")
  }

  fun showToast(msg: String) {
    _uiState.update { it.copy(toastMessage = msg) }
  }

  fun clearToast() {
    _uiState.update { it.copy(toastMessage = null) }
  }
}
