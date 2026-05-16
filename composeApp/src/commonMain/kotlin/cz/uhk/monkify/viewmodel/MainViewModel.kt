package cz.uhk.monkify.viewmodel

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Severity
import cz.uhk.monkify.database.model.DailyTask
import cz.uhk.monkify.model.CategoryTask
import cz.uhk.monkify.preferences.PreferencesManager
import cz.uhk.monkify.repository.DailyTaskRepository
import cz.uhk.monkify.repository.UserStatsRepository
import cz.uhk.monkify.service.FirestoreSyncManager
import cz.uhk.monkify.service.StreakManager
import cz.uhk.monkify.util.AppLog
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel :
    ViewModel(),
    KoinComponent {
    private val log = AppLog.logger<MainViewModel>(level = Severity.Info)

    private val firebaseAuth: FirebaseAuth by inject()
    private val preferences: PreferencesManager by inject()
    private val streakManager: StreakManager by inject()
    private val repository: DailyTaskRepository by inject()
    private val userStatsRepository: UserStatsRepository by inject()
    private val firestoreSyncManager: FirestoreSyncManager by inject()

    private val _isAuthenticated = MutableStateFlow<Boolean?>(null)
    val isAuthenticated: StateFlow<Boolean?> = _isAuthenticated.asStateFlow()

    private val _onboardingCompleted = MutableStateFlow<Boolean?>(null)
    val onboardingCompleted: StateFlow<Boolean?> = _onboardingCompleted.asStateFlow()

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        viewModelScope.launch {
            _isAuthenticated.value = firebaseAuth.currentUser != null
            firebaseAuth.authStateChanged.collectLatest { user ->
                log.i { "User changed ${user?.uid}" }
                _isAuthenticated.value = user != null
            }
        }
        viewModelScope.launch {
            preferences.getValue(PreferencesManager.ONBOARDING_COMPLETED, false).collectLatest { completed ->
                _onboardingCompleted.value = completed
            }
        }

        viewModelScope.launch {
            streakManager.checkStreakUpdate()
        }
        viewModelScope.launch {
            seedDebugTasksIfNeeded()
        }
        firestoreSyncManager.start()
    }

    fun setOnboardingCompleted() {
        viewModelScope.launch {
            preferences.setValue(PreferencesManager.ONBOARDING_COMPLETED, true)
        }
    }

    fun logout() {
        viewModelScope.launch {
            firebaseAuth.signOut()
        }
    }

    fun resetProgress() {
        viewModelScope.launch {
            userStatsRepository.setDaysCompleted(0)
            repository.deleteAllInfo()
        }
    }

    private suspend fun seedDebugTasksIfNeeded() {
        if (!SEED_DEBUG_TASKS) return
        if (repository.getArticles().first().isNotEmpty()) return

        debugTasks.forEach { task ->
            repository.upsertInfo(task)
        }
    }

    // todo might remove it dont like it
    private companion object {
        const val SEED_DEBUG_TASKS = false

        val debugTasks = listOf(
            DailyTask(descriptionText = "Drink a glass of water", categoryTask = CategoryTask.Other.name),
            DailyTask(descriptionText = "Stretch for 5 minutes", categoryTask = CategoryTask.Exercise.name),
            DailyTask(descriptionText = "Read 10 pages", categoryTask = CategoryTask.Reading.name),
            DailyTask(descriptionText = "Meditate for 5 minutes", categoryTask = CategoryTask.Meditating.name),
            DailyTask(descriptionText = "Plan tomorrow in notes", categoryTask = CategoryTask.Studying.name),
        )
    }
}
