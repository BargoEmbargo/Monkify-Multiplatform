package cz.uhk.monkify.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import cz.uhk.monkify.common.GlassmorpismCard
import cz.uhk.monkify.common.HeaderTitle
import cz.uhk.monkify.common.HomeCardTitle
import cz.uhk.monkify.common.LoadingScreen
import cz.uhk.monkify.common.StreakCalendar
import cz.uhk.monkify.theme.MonkifyTheme
import cz.uhk.monkify.wrapper.ScreenContentWrapper
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDate
import monkifymultiplatform.composeapp.generated.resources.Res
import monkifymultiplatform.composeapp.generated.resources.welcome_back
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = koinViewModel()) {
    val daysCompleted by viewModel.daysCompleted.collectAsState()
    val streakDates by viewModel.streakDates.collectAsState()
    daysCompleted?.let { HomeScreeContent(daysCompleted = it, streakDates = streakDates) } ?: LoadingScreen()
}

@OptIn(ExperimentalTime::class)
@Composable
private fun HomeScreeContent(daysCompleted: Int, streakDates: Set<LocalDate>) {
    ScreenContentWrapper(isScrollable = true) {
        HeaderTitle(stringResource(Res.string.welcome_back))
        GlassmorpismCard {
            Column {
                HomeCardTitle(daysCompleted)
                StreakCalendar(
                    streakDates = streakDates,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreeContentPreview() {
    MonkifyTheme {
        HomeScreeContent(10, HomeScreenPreviewData.previewStreakDates())
    }
}
