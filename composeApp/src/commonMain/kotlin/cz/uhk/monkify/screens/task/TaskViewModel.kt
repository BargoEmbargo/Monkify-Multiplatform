package cz.uhk.monkify.screens.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Severity
import cz.uhk.monkify.database.model.DailyTask
import cz.uhk.monkify.repository.DailyTaskRepository
import cz.uhk.monkify.util.AppLog
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: DailyTaskRepository) : ViewModel() {
    private val log = AppLog.logger<TaskViewModel>(level = Severity.Info)

    private val _dailyTasks = MutableStateFlow<List<DailyTask>>(emptyList())
    val dailyTasks: StateFlow<List<DailyTask>> = _dailyTasks.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getArticles().collectLatest { tasks ->
                _dailyTasks.value = tasks
            }
        }
    }

    fun upsertInfo(info: DailyTask) = viewModelScope.launch { repository.upsertInfo(info) }
    fun deleteInfo(info: DailyTask) = viewModelScope.launch { repository.deleteInfo(info) }
    fun deleteAllInfo() = viewModelScope.launch { repository.deleteAllInfo() }
    fun updateInfoList(newList: List<DailyTask>) {
        _dailyTasks.value = newList
    }

    suspend fun getInfoById(id: Int): DailyTask = viewModelScope.async {
        repository.getInfoById(id)
    }.await()
}
