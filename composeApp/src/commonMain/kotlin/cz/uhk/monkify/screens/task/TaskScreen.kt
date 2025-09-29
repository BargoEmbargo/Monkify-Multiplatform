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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.uhk.monkify.common.BackTopBar
import cz.uhk.monkify.model.CategoryTask
import cz.uhk.monkify.theme.MonkifyTheme
import cz.uhk.monkify.wrapper.ScreenContentWrapper
import monkifymultiplatform.composeapp.generated.resources.Res
import monkifymultiplatform.composeapp.generated.resources.add_new_task
import monkifymultiplatform.composeapp.generated.resources.add_task
import monkifymultiplatform.composeapp.generated.resources.category
import monkifymultiplatform.composeapp.generated.resources.choose_category
import monkifymultiplatform.composeapp.generated.resources.delete_plan
import monkifymultiplatform.composeapp.generated.resources.description
import monkifymultiplatform.composeapp.generated.resources.enter_description
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TaskScreen(navController: NavController) {
    TaskScreenContent(
        onAddTask = {},
        onDeletePlan = {},
        onNavigateBack = { navController.popBackStack() },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskScreenContent(
    onAddTask: () -> Unit,
    onDeletePlan: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = { BackTopBar(onNavigateBack = onNavigateBack) },
    ) { paddingValues ->
        ScreenContentWrapper(modifier = Modifier.padding(paddingValues)) {
            var description by rememberSaveable { mutableStateOf("") }
            var category by rememberSaveable { mutableStateOf(CategoryTask.Exercise) }
            var categoryExpanded by remember { mutableStateOf(false) }

            Column(modifier = Modifier.fillMaxWidth()) {
                AddTaskTitle()
                Spacer(Modifier.height(16.dp))
                DescriptionInput(description = description, onDescriptionChange = { description = it })
                Spacer(Modifier.height(16.dp))
                CategoryDropdown(
                    category = category,
                    onCategoryChange = { category = it },
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it },
                )
            }
            TaskActionButtons(
                description = description,
                onAddTask = onAddTask,
                onDeletePlan = onDeletePlan,
            )
        }
    }
}

@Composable
private fun AddTaskTitle() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(Res.string.add_new_task),
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
    description: String,
    onAddTask: () -> Unit,
    onDeletePlan: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        Button(
            modifier = Modifier.weight(1f).height(56.dp),
            onClick = { onAddTask() },
            shape = MaterialTheme.shapes.small,
            enabled = description.isNotBlank(),
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
            Spacer(Modifier.width(8.dp))
            Text(stringResource(Res.string.add_task))
        }

        Button(
            modifier = Modifier.weight(1f).height(56.dp),
            onClick = { onDeletePlan() },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            ),
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            Spacer(Modifier.width(8.dp))
            Text(stringResource(Res.string.delete_plan))
        }
    }
}

@Composable
@Preview
private fun TaskScreenContentPreview() {
    MonkifyTheme {
        TaskScreenContent(onAddTask = {}, onDeletePlan = {}, onNavigateBack = {})
    }
}
