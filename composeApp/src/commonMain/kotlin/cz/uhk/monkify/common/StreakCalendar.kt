package cz.uhk.monkify.common

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.minusDays
import com.kizitonwose.calendar.core.minusMonths
import com.kizitonwose.calendar.core.now
import com.kizitonwose.calendar.core.plusMonths
import cz.uhk.monkify.theme.MonkifyTheme
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalTime::class)
@Composable
fun StreakCalendar(
    streakDates: Set<LocalDate>,
    startMonth: YearMonth = YearMonth.now().minusMonths(12),
    endMonth: YearMonth = YearMonth.now().plusMonths(12),
    firstDayOfWeek: DayOfWeek = firstDayOfWeekFromLocale(),
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val currentYearMonth = remember { YearMonth.now() }

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentYearMonth,
        firstDayOfWeek = firstDayOfWeek,
        outDateStyle = OutDateStyle.EndOfRow,
    )

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        val visibleMonth = calendarState.firstVisibleMonth
        MonthHeader(
            month = visibleMonth,
            onPrevious = {
                val prevMonth = visibleMonth.yearMonth.minusMonths(1)
                scope.launch {
                    calendarState.scrollToMonth(prevMonth)
                }
            },
            onNext = {
                val nextMonth = visibleMonth.yearMonth.plusMonths(1)
                scope.launch {
                    calendarState.scrollToMonth(nextMonth)
                }
            },
        )

        Spacer(modifier = Modifier.height(8.dp))

        DaysOfWeekTitleRow(firstDayOfWeek = firstDayOfWeek, modifier = Modifier.padding(vertical = 2.dp))

        HorizontalCalendar(
            state = calendarState,
            dayContent = { day ->
                DayCell(
                    day = day,
                    isStreak = day.position == DayPosition.MonthDate && day.date in streakDates,
                )
            },
        )
    }
}

@Composable
private fun MonthHeader(
    month: CalendarMonth,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    val title = "${month.yearMonth.month.name.lowercase().replaceFirstChar { it.titlecase() }} ${month.yearMonth.year}"
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextButton(onClick = onPrevious) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = Icons.AutoMirrored.Filled.KeyboardArrowLeft.name,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = Icons.Default.CalendarMonth.name,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        TextButton(onClick = onNext) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = Icons.AutoMirrored.Filled.KeyboardArrowRight.name,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun DaysOfWeekTitleRow(firstDayOfWeek: DayOfWeek, modifier: Modifier = Modifier) {
    val daysOfWeek = daysOfWeek(firstDayOfWeek)
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        daysOfWeek.forEach { dow ->
            Text(
                text = dow.name.take(3),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun DayCell(
    day: CalendarDay,
    isStreak: Boolean,
    modifier: Modifier = Modifier,
) {
    // Fade out dates outside the current month.
    val isOutOfMonth = day.position != DayPosition.MonthDate

    Box(
        modifier = modifier.fillMaxWidth().size(60.dp),
        contentAlignment = Alignment.Center,
    ) {
        val textStyle = if (isOutOfMonth) {
            MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f))
        } else {
            MaterialTheme.typography.labelLarge
        }

        if (isStreak) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
            )
            Text(
                text = day.date.day.toString(),
                style = textStyle.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                ),
            )
        } else {
            Text(
                text = day.date.day.toString(),
                style = textStyle,
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun StreakCalendarPreview() {
    val streakDates = remember {
        val today = LocalDate.now()
        val dates = mutableSetOf<LocalDate>()
        for (i in 0..10) {
            dates.add(today.minusDays(i.toLong().toInt()))
        }

        dates.add(today.minusDays(15))
        dates.add(today.minusDays(20))
        dates.add(today.minusDays(25))
        dates
    }
    MonkifyTheme {
        Surface {
            StreakCalendar(
                streakDates = streakDates,
                startMonth = YearMonth.now().minusMonths(3),
                endMonth = YearMonth.now().plusMonths(3),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
