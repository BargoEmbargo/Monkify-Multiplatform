package cz.uhk.monkify.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Severity
import cz.uhk.monkify.preferences.PreferencesManager
import cz.uhk.monkify.util.AppLog
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate
import com.kizitonwose.calendar.core.minusDays
import com.kizitonwose.calendar.core.now
import kotlin.time.ExperimentalTime

class HomeViewModel(preferencesManager: PreferencesManager) : ViewModel() {
    private val log = AppLog.logger<HomeViewModel>(level = Severity.Info)

    val daysCompleted: StateFlow<Int?> =
        preferencesManager.getValue(PreferencesManager.DAYS_COMPLETED, 0)
            .map { value -> value as Int? }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null,
            )

    @OptIn(ExperimentalTime::class)
    val streakDates: StateFlow<Set<LocalDate>> = daysCompleted.map { count ->
        val today = LocalDate.now()
        if (count == null || count <= 0) emptySet() else (0 until count).map { today.minusDays(it) }.toSet()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptySet(),
    )
}
