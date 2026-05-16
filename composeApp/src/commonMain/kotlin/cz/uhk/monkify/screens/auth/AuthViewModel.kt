package cz.uhk.monkify.screens.auth

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Severity
import cz.uhk.monkify.database.remote.FirestoreTaskRepository
import cz.uhk.monkify.preferences.PreferencesManager
import cz.uhk.monkify.repository.DailyTaskRepository
import cz.uhk.monkify.repository.UserStatsRepository
import cz.uhk.monkify.util.AppLog
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

// Holds form state for both sign in and sign up
data class AuthFormState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
)

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
)

class AuthViewModel :
    ViewModel(),
    KoinComponent {
    private val firebaseAuth: FirebaseAuth by inject()
    private val firestoreTaskRepository: FirestoreTaskRepository by inject()
    private val dailyTaskRepository: DailyTaskRepository by inject()
    private val preferencesManager: PreferencesManager by inject()
    private val userStatsRepository: UserStatsRepository by inject()
    private val log = AppLog.logger<AuthViewModel>(level = Severity.Info)

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> get() = _uiState

    fun signIn(email: String, password: String) {
        _uiState.value = AuthUiState(isLoading = true)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                preferencesManager.setValue(PreferencesManager.AUTHENTICATED, true)
                val userId = firebaseAuth.currentUser?.uid
                if (userId != null) {
                    val remoteTasks = firestoreTaskRepository.getTasks(userId)
                    dailyTaskRepository.deleteAllLocal()
                    remoteTasks.forEach { dailyTaskRepository.upsertInfo(it) }
                    userStatsRepository.pullFromRemote()
                }
                _uiState.value = AuthUiState(isSuccess = true)
                log.d { "User signed in and tasks/stats synced" }
            } catch (e: Exception) {
                _uiState.value = AuthUiState(errorMessage = e.message)
            }
        }
    }

    fun signUp(email: String, password: String) {
        _uiState.value = AuthUiState(isLoading = true)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                preferencesManager.setValue(PreferencesManager.AUTHENTICATED, true)
                userStatsRepository.syncToRemote()
                _uiState.value = AuthUiState(isSuccess = true)
                log.d { "User signed up and stats pushed" }
            } catch (e: Exception) {
                _uiState.value = AuthUiState(errorMessage = e.message)
            }
        }
    }

    fun clearState() {
        _uiState.value = AuthUiState()
    }
}
