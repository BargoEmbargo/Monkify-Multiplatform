package cz.uhk.monkify.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.datetime.LocalDate
import com.kizitonwose.calendar.core.minusDays
import com.kizitonwose.calendar.core.now
import kotlin.time.ExperimentalTime

object HomeScreenPreviewData {
    @OptIn(ExperimentalTime::class)
    @Composable
    fun previewStreakDates(): Set<LocalDate> = remember {
        val today = LocalDate.now()
        val dates = mutableSetOf<LocalDate>()
        for (i in 0..10) {
            dates.add(today.minusDays(i))
        }

        dates.add(today.minusDays(15))
        dates.add(today.minusDays(20))
        dates.add(today.minusDays(25))
        dates
    }
}
