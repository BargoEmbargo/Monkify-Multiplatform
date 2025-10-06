package cz.uhk.monkify.screens.plan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Severity
import cz.uhk.monkify.database.model.DailyTask
import cz.uhk.monkify.preferences.PreferencesManager
import cz.uhk.monkify.repository.DailyTaskRepository
import cz.uhk.monkify.service.StreakManager
import cz.uhk.monkify.util.AppLog
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlanViewModel(
    private val repository: DailyTaskRepository,
    private val preferencesManager: PreferencesManager,
    private val streakManager: StreakManager,
) : ViewModel() {
    private val log = AppLog.logger<PlanViewModel>(level = Severity.Info)

    private val _dailyTasks = MutableStateFlow<List<DailyTask>>(emptyList())
    val dailyTasks: StateFlow<List<DailyTask>> = _dailyTasks.asStateFlow()

    private val _isLocked = MutableStateFlow(false)
    val isLocked: StateFlow<Boolean> = _isLocked.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<Unit>()
    val snackbarEvent: SharedFlow<Unit> = _snackbarEvent

    private val _showAllTasksCompletedDialog = MutableStateFlow(false)
    val showAllTasksCompletedDialog: StateFlow<Boolean> = _showAllTasksCompletedDialog.asStateFlow()
    val achievementProgress: StateFlow<AchievementProgress> = _dailyTasks
        .map { tasks ->
            val completed = tasks.count { it.isChecked }
            val total = tasks.size
            val uncompleted = total - completed
            val percent = if (total > 0) (completed * 100 / total) else 0
            val pieData = listOf(completed, uncompleted)
            AchievementProgress(completed, total, uncompleted, percent, pieData)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, AchievementProgress(0, 0, 0, 0, listOf(0, 0)))

    init {
//        // THIS IS ONLY FOR TESTING, RESET DAYS COMPLETED TO 0!
//        viewModelScope.launch {
//            preferencesManager.setValue(
//                PreferencesManager.DAYS_COMPLETED,
//                0,
//            )
//            val current = preferencesManager.getValue(PreferencesManager.DAYS_COMPLETED, 0).first()
//            log.i { "Reseted to: $current" }
//        }
        viewModelScope.launch {
            repository.getArticles().collectLatest { tasks ->
                _dailyTasks.value = tasks

                val allChecked = tasks.isNotEmpty() && tasks.all { it.isChecked }
                _isLocked.value = allChecked
            }
        }
    }

    fun toggleTaskChecked(taskId: Int) {
        if (_isLocked.value) {
            emitAllTasksCompletedSnackbar()
            return
        }
        val task = _dailyTasks.value.find { it.id == taskId } ?: return
        val updatedTask = task.copy(isChecked = !task.isChecked)
        viewModelScope.launch {
            repository.upsertInfo(updatedTask)
            val updatedTasks = _dailyTasks.value.map {
                if (it.id == taskId) updatedTask else it
            }
            val allChecked = updatedTasks.all { it.isChecked }
            if (allChecked) {
                handleAllTasksChecked(updatedTasks)
            }
        }
    }

    private fun emitAllTasksCompletedSnackbar() {
        viewModelScope.launch {
            _snackbarEvent.emit(Unit)
        }
    }

    fun resetAllTasksCompletedDialog() {
        _showAllTasksCompletedDialog.value = false
    }

    private suspend fun handleAllTasksChecked(updatedTasks: List<DailyTask>) {
        if (updatedTasks.isNotEmpty()) {
            val current = preferencesManager.getValue(PreferencesManager.DAYS_COMPLETED, 0).first()
            preferencesManager.setValue(PreferencesManager.DAYS_COMPLETED, current + 1)
            streakManager.updateActivityDate()
            log.i { "All tasks completed! Incremented DAYS_COMPLETED to ${current + 1}" }
            _isLocked.value = true
            _showAllTasksCompletedDialog.value = true
        }
    }
}
