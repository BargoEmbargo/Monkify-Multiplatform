package cz.uhk.monkify.screens.plan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.uhk.monkify.common.GlassmorpismCard
import cz.uhk.monkify.common.PieChart
import cz.uhk.monkify.theme.MonkifyTheme
import cz.uhk.monkify.wrapper.ScreenContentWrapper
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PlanScreen(navController: NavController) {
    PlanScreenContent(
        onSetupClick = { /* TODO navigate to setup screen */ },
    )
}

@Composable
private fun PlanScreenContent(onSetupClick: () -> Unit) {
    ScreenContentWrapper(isScrollable = true, showScrollbar = true) {
        PlanHeader()
        AchievementCard()
        DailyGoalsHeader(onSetupClick = { onSetupClick() })
        GlassmorpismCard {
            DailyGoalsSection() // todo replace with real data
        }
    }
}

@Composable
private fun PlanHeader() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Here is your Plan:",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 10.dp, top = 8.dp),
        )
    }
}

@Composable
private fun AchievementCard() {
    val isPreview = LocalInspectionMode.current
    GlassmorpismCard {
        Column {
            Text(
                text = "Achievement",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(0.4f)) {
                    AchievementLegend(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        text = "You're making progress! 9 days down, out of 12. You're 72% there!", // todo change text
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AchievementLegend(
                        color = MaterialTheme.colorScheme.secondary,
                        text = "Days left. In order to complete the challenge you have 3 days remaining", // todo change text
                    )
                }

                PieChart(
                    modifier = Modifier.padding(end = 4.dp),
                    data = mapOf("Progress" to 9, "Left" to 3),
                    progressOverride = if (isPreview) 1f else null
                )
            }
        }
    }
}

@Composable
private fun AchievementLegend(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(0.9f)) {
        Box(
            modifier = Modifier.size(12.dp)
                .background(color = color, shape = MaterialTheme.shapes.small),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun DailyGoalsHeader(onSetupClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Daily goals",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "Set up",
            style = MaterialTheme.typography.labelLarge.copy(textDecoration = TextDecoration.Underline),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onSetupClick() },
        )
    }
}

@Composable
private fun DailyGoalsSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            DailyGoalItem("Read atleast 10 pages", checked = true)
            DailyGoalItem("No more than 2 hours of social media", checked = true)
            DailyGoalItem("Exercise", checked = false)
        }
    }
}

@Composable
private fun DailyGoalItem(text: String, checked: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .background(
                    color = if (checked) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = RoundedCornerShape(6.dp),
                )
                .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(6.dp)),
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 12.dp),
        )
    }
}

@Preview
@Composable
private fun PlanScreenPreview() {
    MonkifyTheme {
        PlanScreenContent(onSetupClick = { })
    }
}
