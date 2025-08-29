package cz.uhk.monkify.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.uhk.monkify.database.model.DailyTask
import cz.uhk.monkify.screens.home.HomeViewModel
import cz.uhk.monkify.wrapper.ScreenContentWrapper
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Preview
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = koinViewModel()) {
    val tasks by viewModel.tasks.collectAsState()
    HomeScreeContent(tasks = tasks, viewModel = viewModel)
}

@Composable
fun HomeScreeContent(tasks: List<DailyTask>, viewModel: HomeViewModel) {
    ScreenContentWrapper(
        isScrollable = false, // Set to false as LazyColumn handles scrolling
        showScrollbar = false,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = {
                // For simplicity, adding a predefined task.
                // You can expand this to include input fields for custom tasks.
                viewModel.addTask(descriptionText = "New custom task", categoryTask = "General")
            }) {
                Text("Add New Task")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (tasks.isEmpty()) {
                Text("No tasks yet. Add some!")
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(tasks) { task ->
                        Text("Task: ${task.descriptionText}, Category: ${task.categoryTask}")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}
