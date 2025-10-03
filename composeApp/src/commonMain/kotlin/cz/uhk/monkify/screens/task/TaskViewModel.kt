package cz.uhk.monkify.screens.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Severity
import cz.uhk.monkify.database.model.DailyTask
import cz.uhk.monkify.model.CategoryTask
import cz.uhk.monkify.repository.DailyTaskRepository
import cz.uhk.monkify.util.AppLog
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: DailyTaskRepository) : ViewModel() {
    private val log = AppLog.logger<TaskViewModel>(level = Severity.Info)

    private val _taskScreenUiState = MutableStateFlow(TaskScreenUiState())
    val taskScreenUiState: StateFlow<TaskScreenUiState> = _taskScreenUiState.asStateFlow()

    private fun upsertInfo(info: DailyTask) = viewModelScope.launch { repository.upsertInfo(info) }
    private fun deleteInfo(info: DailyTask) = viewModelScope.launch { repository.deleteInfo(info) }
    fun deleteAllInfo() = viewModelScope.launch { repository.deleteAllInfo() }

    private suspend fun getInfoById(id: Int): DailyTask = viewModelScope.async { repository.getInfoById(id) }.await()

    fun loadTaskForEdit(taskId: Int) {
        if (taskId == -1) {
            _taskScreenUiState.value = TaskScreenUiState(isEditMode = false)
        } else {
            viewModelScope.launch {
                val item = getInfoById(taskId)
                _taskScreenUiState.value = TaskScreenUiState(
                    currentId = taskId,
                    description = item.descriptionText,
                    category = CategoryTask.entries.find { it.name.equals(item.categoryTask, ignoreCase = true) } ?: CategoryTask.Exercise,
                    isEditMode = true,
                )
                log.i { "Loaded task item with ID:${_taskScreenUiState.value.currentId}" }
            }
        }
    }

    private fun addOrUpdateTask() {
        val state = _taskScreenUiState.value
        if (state.isEditMode) {
            // Update
            val task = DailyTask(
                id = state.currentId,
                descriptionText = state.description,
                categoryTask = state.category.name,
            )
            upsertInfo(task)
        } else {
            // Add
            val task = DailyTask(
                descriptionText = state.description,
                categoryTask = state.category.name,
            )
            upsertInfo(task)
        }
    }

    private fun deleteSingleTask() {
        val state = _taskScreenUiState.value
        if (state.isEditMode) {
            viewModelScope.launch {
                val item = getInfoById(state.currentId)
                deleteInfo(item)
            }
        }
    }

    private fun updateDescription(newDescription: String) {
        _taskScreenUiState.update { it.copy(description = newDescription) }
    }

    private fun updateCategory(newCategory: CategoryTask) {
        _taskScreenUiState.update { it.copy(category = newCategory) }
    }

    fun onEvent(event: TaskUiEvent) {
        when (event) {
            is TaskUiEvent.DescriptionChanged -> updateDescription(event.value)
            is TaskUiEvent.CategoryChanged -> updateCategory(event.value)
            is TaskUiEvent.LoadForEdit -> loadTaskForEdit(event.taskId)
            is TaskUiEvent.AddOrUpdate -> addOrUpdateTask()
            is TaskUiEvent.DeleteSingleItem -> deleteSingleTask()
            is TaskUiEvent.DeleteAll -> {}
            is TaskUiEvent.NavigateBack -> {}
        }
    }
}
