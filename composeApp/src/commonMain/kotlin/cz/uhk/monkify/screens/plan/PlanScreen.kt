package cz.uhk.monkify.screens.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.uhk.monkify.common.GlassmorpismCard
import cz.uhk.monkify.common.HeaderTitle
import cz.uhk.monkify.common.PieChart
import cz.uhk.monkify.common.dialogs.TaskCheckingInfoDialog
import cz.uhk.monkify.database.model.DailyTask
import cz.uhk.monkify.model.CategoryTask
import cz.uhk.monkify.navigation.NavigationGraph
import cz.uhk.monkify.theme.MonkifyTheme
import cz.uhk.monkify.wrapper.ScreenContentWrapper
import monkifymultiplatform.composeapp.generated.resources.Res
import monkifymultiplatform.composeapp.generated.resources.achievement_days_left
import monkifymultiplatform.composeapp.generated.resources.achievement_progress
import monkifymultiplatform.composeapp.generated.resources.achievement_title
import monkifymultiplatform.composeapp.generated.resources.daily_goals_header
import monkifymultiplatform.composeapp.generated.resources.daily_goals_setup
import monkifymultiplatform.composeapp.generated.resources.empty_no_data
import monkifymultiplatform.composeapp.generated.resources.empty_set_up_goal
import monkifymultiplatform.composeapp.generated.resources.plan_header
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PlanScreen(navController: NavController, viewModel: PlanViewModel = koinViewModel()) {
    val dailyTasks by viewModel.dailyTasks.collectAsState()
    val achievementProgress by viewModel.achievementProgress.collectAsState()
    PlanScreenContent(
        onSetupClick = { navController.navigate(NavigationGraph.TaskScreen.name) },
        onCheckedChange = { id -> viewModel.toggleTaskChecked(id) },
        onRowClick = { id -> navController.navigate(NavigationGraph.TaskScreen.name + "?taskId=$id") },
        dailyTasks = dailyTasks,
        achievementProgress = achievementProgress,
    )
}

@Composable
private fun PlanScreenContent(
    dailyTasks: List<DailyTask>,
    achievementProgress: AchievementProgress,
    onSetupClick: () -> Unit,
    onCheckedChange: (Int) -> Unit,
    onRowClick: (Int) -> Unit,
) {
    Scaffold { paddingValues ->
        ScreenContentWrapper(
            modifier = Modifier.padding(paddingValues),
            isScrollable = true,
            showScrollbar = true,
        ) {
            HeaderTitle(stringResource(Res.string.plan_header))
            AchievementCard(achievementProgress = achievementProgress, dailyTasksEmpty = dailyTasks.isEmpty())
            DailyGoalsHeader(onSetupClick = { onSetupClick() })
            GlassmorpismCard {
                if (dailyTasks.isEmpty()) {
                    EmptyText(text = stringResource(Res.string.empty_set_up_goal))
                } else {
                    DailyGoalsSection(
                        dailyTasks = dailyTasks,
                        onCheckedChange = onCheckedChange,
                        onRowClick = onRowClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun AchievementCard(achievementProgress: AchievementProgress, dailyTasksEmpty: Boolean) {
    val isPreview = LocalInspectionMode.current
    GlassmorpismCard {
        if (dailyTasksEmpty) {
            EmptyText(text = stringResource(Res.string.empty_no_data))
        } else {
            Column {
                Text(
                    text = stringResource(Res.string.achievement_title),
                    style = MaterialTheme.typography.titleMedium,
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(0.4f)) {
                        AchievementLegend(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            text = stringResource(
                                Res.string.achievement_progress,
                                achievementProgress.completed,
                                achievementProgress.total,
                                achievementProgress.percent,
                            ),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AchievementLegend(
                            color = MaterialTheme.colorScheme.secondary,
                            text = stringResource(
                                Res.string.achievement_days_left,
                                achievementProgress.uncompleted,
                            ),
                        )
                    }

                    PieChart(
                        modifier = Modifier.padding(end = 6.dp, bottom = 6.dp),
                        data = achievementProgress.pieData,
                        progressOverride = if (isPreview) 1f else null,
                    )
                }
            }
        }
    }
}

@Composable
private fun AchievementLegend(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(0.9f)) {
        Box(modifier = Modifier.size(12.dp).background(color = color, shape = MaterialTheme.shapes.small))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun DailyGoalsHeader(onSetupClick: () -> Unit) {
    var showInfoDialog by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(Res.string.daily_goals_header),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable { showInfoDialog = true },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = Icons.Filled.Info.name,
                    tint = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                )
            }
        }
        Text(
            text = stringResource(Res.string.daily_goals_setup),
            style = MaterialTheme.typography.labelLarge.copy(textDecoration = TextDecoration.Underline),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onSetupClick() },
        )
    }
    if (showInfoDialog) {
        TaskCheckingInfoDialog(
            onDismiss = { showInfoDialog = false },
        )
    }
}

@Composable
private fun DailyGoalsSection(
    dailyTasks: List<DailyTask>,
    onCheckedChange: (Int) -> Unit,
    onRowClick: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            dailyTasks.forEachIndexed { index, task ->
                DailyGoalItem(
                    number = index + 1,
                    text = task.descriptionText,
                    checked = task.isChecked,
                    onCheckedChange = { onCheckedChange(task.id) },
                    onRowClick = { onRowClick(task.id) },
                    category = task.categoryTask,
                )
            }
        }
    }
}

@Composable
private fun DailyGoalItem(
    number: Int,
    text: String,
    checked: Boolean,
    onCheckedChange: () -> Unit,
    onRowClick: () -> Unit,
    category: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clip(MaterialTheme.shapes.medium).clickable(onClick = onRowClick),
    ) {
        Text(
            text = "$number.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 8.dp),
        )
        Checkbox(
            checked = checked,
            onCheckedChange = { onCheckedChange() },
            modifier = Modifier.padding(0.dp),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 12.dp),
        )
        Spacer(modifier = Modifier.weight(1f))
        val categoryEnum = CategoryTask.entries.find { it.name.equals(category, ignoreCase = true) } ?: CategoryTask.Other
        Box(
            modifier = Modifier.size(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = categoryEnum.icon,
                contentDescription = categoryEnum.name,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun EmptyText(text: String) {
    Box(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
private fun PlanScreenPreview() {
    MonkifyTheme {
        PlanScreenContent(
            onSetupClick = {},
            dailyTasks = listOf(
                DailyTask(1, false, "Meditate for 10 minutes", "Meditating"),
                DailyTask(2, true, "Read 20 pages of a book", "Studying"),
                DailyTask(3, false, "Exercise for 30 minutes", "Exercise"),
                DailyTask(4, false, "Lock phone for 1 hour", "PhoneLocked"),
                DailyTask(5, false, "Go for a bike ride", "RidingBike"),
            ),
            achievementProgress = AchievementProgress(1, 5, 4, 20, listOf(1, 4)),
            onCheckedChange = {},
            onRowClick = {},
        )
    }
}

@Preview
@Composable
private fun PlanScreenEmptyPreview() {
    MonkifyTheme {
        PlanScreenContent(
            onSetupClick = {},
            dailyTasks = emptyList(),
            achievementProgress = AchievementProgress(0, 0, 0, 0, listOf(0, 0)),
            onCheckedChange = {},
            onRowClick = {},
        )
    }
}
