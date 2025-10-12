package cz.uhk.monkify.screens.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.uhk.monkify.common.BackTopBar
import cz.uhk.monkify.common.dialogs.DeleteConfirmationDialog
import cz.uhk.monkify.extension.applyHorizontalScreenPadding
import cz.uhk.monkify.model.CategoryTask
import cz.uhk.monkify.theme.MonkifyTheme
import cz.uhk.monkify.wrapper.ScreenContentWrapper
import cz.uhk.monkify.wrapper.ScreenHorizontalPaddingClass
import kotlinx.coroutines.launch
import monkifymultiplatform.composeapp.generated.resources.Res
import monkifymultiplatform.composeapp.generated.resources.add_new_task
import monkifymultiplatform.composeapp.generated.resources.category
import monkifymultiplatform.composeapp.generated.resources.choose_category
import monkifymultiplatform.composeapp.generated.resources.description
import monkifymultiplatform.composeapp.generated.resources.edit_task
import monkifymultiplatform.composeapp.generated.resources.enter_description
import monkifymultiplatform.composeapp.generated.resources.snackbar_task_added
import monkifymultiplatform.composeapp.generated.resources.snackbar_task_updated
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TaskScreen(
    navController: NavController,
    taskId: Int,
    viewModel: TaskViewModel = koinViewModel(),
) {
    val uiState by viewModel.taskScreenUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage = stringResource(if (uiState.isEditMode) Res.string.snackbar_task_updated else Res.string.snackbar_task_added)

    LaunchedEffect(taskId) {
        // todo dunno if i like this? consider koin injection of viewModel with parameters
        viewModel.onEvent(TaskUiEvent.LoadForEdit(taskId))
    }

    TaskScreenContent(
        uiState = uiState,
        onEvent = { event ->
            viewModel.onEvent(event)
            when (event) {
                is TaskUiEvent.AddOrUpdate -> {
                    coroutineScope.launch { snackbarHostState.showSnackbar(snackbarMessage) }
                }
                is TaskUiEvent.DeleteAll -> {
                    showDeleteDialog = true
                }
                is TaskUiEvent.DeleteSingleItem -> {
                    navController.popBackStack()
                }
                is TaskUiEvent.NavigateBack -> {
                    navController.popBackStack()
                }
                else -> {}
            }
        },
        snackbarHostState = snackbarHostState,
    )

    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            onConfirm = {
                viewModel.deleteAllInfo()
                navController.popBackStack()
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskScreenContent(
    uiState: TaskScreenUiState,
    onEvent: (TaskUiEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    var categoryExpanded by remember { mutableStateOf(false) }
    Scaffold(
        topBar = { BackTopBar(onNavigateBack = { onEvent(TaskUiEvent.NavigateBack) }) },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .applyHorizontalScreenPadding(ScreenHorizontalPaddingClass.Half)
                    .padding(bottom = 56.dp),
                hostState = snackbarHostState,
            )
        },
    ) { paddingValues ->
        ScreenContentWrapper(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                bottom = 24.dp,
            ),
            applyVerticalPadding = false,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                AddTaskTitle(uiState.isEditMode)
                Spacer(Modifier.height(24.dp))
                DescriptionInput(
                    description = uiState.description,
                    onDescriptionChange = { onEvent(TaskUiEvent.DescriptionChanged(it)) },
                )
                Spacer(Modifier.height(24.dp))
                CategoryDropdown(
                    category = uiState.category,
                    onCategoryChange = { onEvent(TaskUiEvent.CategoryChanged(it)) },
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it },
                )
            }
            TaskActionButtons(
                isEditMode = uiState.isEditMode,
                description = uiState.description,
                onAddOrUpdateTask = { onEvent(TaskUiEvent.AddOrUpdate) },
                onDelete = { if (uiState.isEditMode) onEvent(TaskUiEvent.DeleteSingleItem) else onEvent(TaskUiEvent.DeleteAll) },
            )
        }
    }
}

@Composable
private fun AddTaskTitle(isEditMode: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(if (isEditMode) Res.string.edit_task else Res.string.add_new_task),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        )
    }
}

@Composable
private fun DescriptionInput(description: String, onDescriptionChange: (String) -> Unit) {
    Text(
        text = stringResource(Res.string.description),
        style = MaterialTheme.typography.titleMedium,
    )
    TextField(
        value = description,
        onValueChange = onDescriptionChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(Res.string.enter_description)) },
        singleLine = true,
        shape = MaterialTheme.shapes.small,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            disabledIndicatorColor = MaterialTheme.colorScheme.outline,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryDropdown(
    category: CategoryTask,
    onCategoryChange: (CategoryTask) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
) {
    Text(
        text = stringResource(Res.string.category),
        style = MaterialTheme.typography.titleMedium,
    )
    Spacer(Modifier.height(8.dp))
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
    ) {
        OutlinedTextField(
            readOnly = true,
            value = stringResource(category.titleRes),
            onValueChange = {},
            label = { Text(stringResource(Res.string.choose_category)) },
            leadingIcon = { Icon(category.icon, contentDescription = category.name) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                .fillMaxWidth(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
        ) {
            CategoryTask.entries.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            Icon(item.icon, contentDescription = item.name)
                            Text(stringResource(item.titleRes))
                        }
                    },
                    onClick = {
                        onCategoryChange(item)
                        onExpandedChange(false)
                    },
                )
            }
        }
    }
}

@Composable
private fun TaskActionButtons(
    isEditMode: Boolean,
    description: String,
    onAddOrUpdateTask: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        Button(
            modifier = Modifier.weight(1f).height(56.dp),
            onClick = onAddOrUpdateTask,
            shape = MaterialTheme.shapes.small,
            enabled = description.isNotBlank(),
        ) {
            Icon(Icons.Default.Add, contentDescription = Icons.Default.Add.name)
            Spacer(Modifier.width(8.dp))
            Text(if (isEditMode) "Update" else "Add")
        }

        Button(
            modifier = Modifier.weight(1f).height(56.dp),
            onClick = onDelete,
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            ),
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = Icons.Default.Delete.name)
            Spacer(Modifier.width(8.dp))
            Text(if (isEditMode) "Delete" else "Delete All")
        }
    }
}

@Composable
@Preview
private fun TaskScreenAddNewItemPreview() {
    MonkifyTheme {
        TaskScreenContent(
            uiState = TaskScreenUiState(
                description = "",
                category = CategoryTask.Exercise,
                isEditMode = false,
            ),
            onEvent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}

@Composable
@Preview
private fun TaskScreenUpdateItemPreview() {
    MonkifyTheme {
        TaskScreenContent(
            uiState = TaskScreenUiState(
                description = "Read a book",
                category = CategoryTask.Studying,
                isEditMode = true,
            ),
            onEvent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}
