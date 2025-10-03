package cz.uhk.monkify.screens.task

import cz.uhk.monkify.model.CategoryTask

data class TaskScreenUiState(
    val currentId: Int = -1,
    val description: String = "",
    val category: CategoryTask = CategoryTask.Exercise,
    val isEditMode: Boolean = false,
)
