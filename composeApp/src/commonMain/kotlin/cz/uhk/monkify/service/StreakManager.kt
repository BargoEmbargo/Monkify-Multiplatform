package cz.uhk.monkify.service

import co.touchlab.kermit.Severity
import com.kizitonwose.calendar.core.now
import cz.uhk.monkify.preferences.PreferencesManager
import cz.uhk.monkify.repository.DailyTaskRepository
import cz.uhk.monkify.util.AppLog
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDate
@OptIn(ExperimentalTime::class)
class StreakManager(private val preferencesManager: PreferencesManager, private val dailyTaskRepository: DailyTaskRepository) {
    private val log = AppLog.logger<StreakManager>(level = Severity.Info)

    suspend fun checkStreakUpdate() {
        if (DEBUG) {
            checkTestInterval()
        } else {
            checkDayChange()
        }
    }

    /**
     * Checks if a new day has started and handles streak logic accordingly.
     * Resets streak if tasks were not completed the previous day.
     * Updates last activity date to today.
     */
    private suspend fun checkDayChange() {
        val today = LocalDate.now()
        val lastActivityDateString = preferencesManager.getValue(
            PreferencesManager.LAST_ACTIVITY_DATE,
            "",
        ).first()

        log.i { "Checking day change. Today: $today, Last activity: $lastActivityDateString" }

        if (lastActivityDateString.isEmpty()) {
            log.i { "First time opening app, setting today as last activity date" }
            preferencesManager.setValue(PreferencesManager.LAST_ACTIVITY_DATE, today.toString())
            return
        }

        val lastActivityDate = try {
            LocalDate.parse(lastActivityDateString)
        } catch (e: Exception) {
            log.e(e) { "Failed to parse last activity date: $lastActivityDateString" }
            preferencesManager.setValue(PreferencesManager.LAST_ACTIVITY_DATE, today.toString())
            return
        }

        // Check if it's a new day
        if (today > lastActivityDate) {
            log.i { "New day detected. Checking if tasks were completed yesterday." }

            // Check if all tasks were completed on the last activity date
            val allTasksCompleted = checkIfAllTasksWereCompleted()

            if (!allTasksCompleted) {
                // Reset streak if tasks weren't completed
                log.i { "Not all tasks were completed yesterday. Resetting streak to 0." }
                preferencesManager.setValue(PreferencesManager.DAYS_COMPLETED, 0)
            } else {
                log.i { "All tasks were completed yesterday. Streak maintained." }
            }

            // Reset all tasks for the new day
            resetTasksForNewDay()

            // Update last activity date to today
            preferencesManager.setValue(PreferencesManager.LAST_ACTIVITY_DATE, today.toString())
        }
    }

    /**
     * Updates the last activity date to today.
     * Called when user completes all tasks.
     */
    suspend fun updateActivityDate() {
        val today = LocalDate.now()
        preferencesManager.setValue(PreferencesManager.LAST_ACTIVITY_DATE, today.toString())
        log.i { "Updated last activity date to: $today" }
    }

    /**
     * Checks if all tasks were completed (all tasks are checked).
     */
    private suspend fun checkIfAllTasksWereCompleted(): Boolean {
        val tasks = dailyTaskRepository.getArticles().first()
        return tasks.isNotEmpty() && tasks.all { it.isChecked }
    }

    /**
     * Resets all tasks to unchecked state for a new day.
     */
    private suspend fun resetTasksForNewDay() {
        log.i { "Resetting all tasks for new day" }
        val tasks = dailyTaskRepository.getArticles().first()
        tasks.forEach { task ->
            if (task.isChecked) {
                val resetTask = task.copy(isChecked = false)
                dailyTaskRepository.upsertInfo(resetTask)
            }
        }
    }

    /**
     * For testing: checks if 20 seconds have passed since last activity, and resets streak if needed after new app open.
     * This does NOT affect production logic
     */
    suspend fun checkTestInterval() {
        val nowMillis = kotlin.time.Clock.System.now().toEpochMilliseconds()
        val lastActivityString = preferencesManager.getValue(
            PreferencesManager.LAST_TEST_ACTIVITY_TIMESTAMP,
            "",
        ).first()
        log.i { "[TEST] Checking interval. Now: $nowMillis, Last: $lastActivityString" }
        if (lastActivityString.isEmpty()) {
            preferencesManager.setValue(PreferencesManager.LAST_TEST_ACTIVITY_TIMESTAMP, nowMillis.toString())
            return
        }
        val lastMillis = lastActivityString.toLongOrNull() ?: nowMillis
        val diff = (nowMillis - lastMillis) / 1000L
        if (diff >= TEST_INTERVAL_SECONDS) {
            log.i { "[TEST] 20 seconds passed. Checking if tasks were completed." }
            val allTasksCompleted = checkIfAllTasksWereCompleted()
            if (!allTasksCompleted) {
                preferencesManager.setValue(PreferencesManager.DAYS_COMPLETED, 0)
                log.i { "[TEST] Not all tasks completed. Streak reset." }
            } else {
                log.i { "[TEST] All tasks completed. Streak maintained." }
            }
            resetTasksForNewDay()
            preferencesManager.setValue(PreferencesManager.LAST_TEST_ACTIVITY_TIMESTAMP, nowMillis.toString())
        } else {
            log.i { "[TEST] Interval not reached. Remaining: ${20L - diff} seconds" }
        }
    }

    companion object {
        private const val DEBUG = false
        private const val TEST_INTERVAL_SECONDS = 20L
    }
}
