package cz.uhk.monkify.screens.plan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Severity
import cz.uhk.monkify.database.model.DailyTask
import cz.uhk.monkify.repository.DailyTaskRepository
import cz.uhk.monkify.util.AppLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PlanViewModel(private val repository: DailyTaskRepository) : ViewModel() {
    private val log = AppLog.logger<PlanViewModel>(level = Severity.Info)

    private val _dailyTasks = MutableStateFlow<List<DailyTask>>(emptyList())
    val dailyTasks: StateFlow<List<DailyTask>> = _dailyTasks.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getArticles().collectLatest { tasks ->
                _dailyTasks.value = tasks
            }
        }
    }

    fun toggleTaskChecked(taskId: Int) {
        val task = _dailyTasks.value.find { it.id == taskId } ?: return
        val updatedTask = task.copy(isChecked = !task.isChecked)
        viewModelScope.launch {
            repository.upsertInfo(updatedTask)
        }
    }
}
