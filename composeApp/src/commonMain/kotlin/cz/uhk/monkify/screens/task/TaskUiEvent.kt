package cz.uhk.monkify.screens.task

import cz.uhk.monkify.model.CategoryTask

sealed class TaskUiEvent {
    data class DescriptionChanged(val value: String) : TaskUiEvent()
    data class CategoryChanged(val value: CategoryTask) : TaskUiEvent()
    data class LoadForEdit(val taskId: Int) : TaskUiEvent()
    object AddOrUpdate : TaskUiEvent()
    object DeleteSingleItem : TaskUiEvent()
    object DeleteAll : TaskUiEvent()
    object NavigateBack : TaskUiEvent()
}
