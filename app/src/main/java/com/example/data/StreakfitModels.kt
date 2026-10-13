package com.example.data

enum class HabitMetricType {
  CHECK_IN,
  COUNT,
  DURATION
}

data class Habit(
  val id: String,
  val title: String,
  val icon: String,
  val streakDays: Int,
  val metricType: HabitMetricType = HabitMetricType.CHECK_IN,
  val currentValue: Int = 0,
  val targetValue: Int = 1,
  val unit: String = "",
  val isCompleted: Boolean = false,
  val subtitle: String = "",
  val isHealthConnectSynced: Boolean = false,
  val isWaterStepper: Boolean = false,
  val reminderTime: String = "08:30 PM",
  val reminderEnabled: Boolean = true,
  val forgivingStreak: Boolean = true,
  val colorTone: String = "emerald"
)

data class ExerciseSet(
  val id: String,
  val setNumber: Int,
  val weightKg: Double,
  val reps: Int,
  val rpe: String? = null,
  val isCompleted: Boolean = false,
  val isPr: Boolean = false,
  val prLabel: String? = null,
  val isPrefilled: Boolean = false,
  val statusNote: String? = null
)

data class WorkoutExercise(
  val id: String,
  val name: String,
  val equipment: String,
  val muscleGroup: String,
  val isActive: Boolean = false,
  val sets: List<ExerciseSet>
)

enum class DayStatus {
  LOGGED,
  MISSED,
  FREEZE,
  TODAY_ACTIVE
}

data class HeatmapDay(
  val dayNumber: Int,
  val status: DayStatus
)

data class WeekCalendarDay(
  val dayLetter: String,
  val dayNumber: Int,
  val isCompleted: Boolean,
  val isToday: Boolean
)

data class DayVolumeStat(
  val dayLabel: String,
  val volumeKg: Double,
  val displayStr: String,
  val isRest: Boolean,
  val isPeak: Boolean = false
)

data class PersonalRecord(
  val id: String,
  val exerciseName: String,
  val weightKg: Int,
  val detail: String,
  val badge: String? = null,
  val bump: String? = null,
  val iconType: String = "dumbbell"
)

enum class StreakfitTab {
  TODAY,
  WORKOUTS,
  HABITS,
  PROGRESS
}

enum class ActiveScreen {
  MAIN_TABS,
  ACTIVE_WORKOUT,
  NEW_HABIT
}
