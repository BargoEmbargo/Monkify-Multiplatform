package cz.uhk.monkify.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.uhk.monkify.database.model.DailyTask
import cz.uhk.monkify.repository.DailyTaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: DailyTaskRepository) : ViewModel() {

    val tasks: StateFlow<List<DailyTask>> = repository.getArticles()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    fun addTask(descriptionText: String, categoryTask: String) {
        viewModelScope.launch {
            if (descriptionText.isNotBlank() && categoryTask.isNotBlank()) {
                repository.upsertInfo(DailyTask(descriptionText = descriptionText, categoryTask = categoryTask))
            }
        }
    }
}
