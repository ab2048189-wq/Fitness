package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.ActiveScreen
import com.example.data.HabitMetricType
import com.example.data.StreakfitTab
import com.example.data.StreakfitViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Streakfit", appName)
  }

  @Test
  fun `test initial state and habit toggle`() {
    val vm = StreakfitViewModel()
    val state = vm.uiState.value
    assertEquals(StreakfitTab.TODAY, state.selectedTab)
    assertEquals(ActiveScreen.MAIN_TABS, state.currentScreen)
    assertEquals(14, state.streakDays)
    assertTrue(state.habits.isNotEmpty())

    val readingHabit = state.habits.find { it.id == "h5" }
    assertNotNull(readingHabit)
    assertFalse(readingHabit!!.isCompleted)

    vm.toggleHabit("h5")
    val updatedHabit = vm.uiState.value.habits.find { it.id == "h5" }
    assertTrue(updatedHabit!!.isCompleted)
  }

  @Test
  fun `test water intake adjustment and completion`() {
    val vm = StreakfitViewModel()
    val waterHabitBefore = vm.uiState.value.habits.find { it.isWaterStepper }
    assertNotNull(waterHabitBefore)
    val initialVal = waterHabitBefore!!.currentValue

    vm.adjustWater(250)
    val waterHabitAfter = vm.uiState.value.habits.find { it.isWaterStepper }
    assertEquals(initialVal + 250, waterHabitAfter!!.currentValue)

    vm.completeWaterGoal()
    val completedWater = vm.uiState.value.habits.find { it.isWaterStepper }
    assertEquals(2500, completedWater!!.currentValue)
    assertTrue(completedWater.isCompleted)
  }

  @Test
  fun `test workout set logging and navigation`() {
    val vm = StreakfitViewModel()
    vm.navigateTo(ActiveScreen.ACTIVE_WORKOUT)
    assertEquals(ActiveScreen.ACTIVE_WORKOUT, vm.uiState.value.currentScreen)

    val initialVolume = vm.uiState.value.liveTotalVolumeKg
    vm.logSet("e2", 1) // Log set 2 of Lateral Raises
    assertTrue(vm.uiState.value.liveTotalVolumeKg > initialVolume)
    assertEquals(90, vm.uiState.value.restSeconds)

    vm.completeWorkoutSession()
    assertEquals(ActiveScreen.MAIN_TABS, vm.uiState.value.currentScreen)
  }

  @Test
  fun `test add new habit routine`() {
    val vm = StreakfitViewModel()
    val initialHabitsCount = vm.uiState.value.habits.size

    vm.addNewHabit(
      title = "Evening Mobility & Foam Rolling",
      icon = "🧘",
      colorTone = "emerald",
      metricType = HabitMetricType.DURATION,
      targetValue = 20,
      unit = "mins",
      reminderTime = "08:30 PM",
      forgivingStreak = true
    )

    val updatedState = vm.uiState.value
    assertEquals(initialHabitsCount + 1, updatedState.habits.size)
    val added = updatedState.habits.last()
    assertEquals("Evening Mobility & Foam Rolling", added.title)
    assertEquals("🧘", added.icon)
    assertEquals(20, added.targetValue)
  }
}
