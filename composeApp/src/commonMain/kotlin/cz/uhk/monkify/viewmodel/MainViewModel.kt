package cz.uhk.monkify.viewmodel

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Severity
import cz.uhk.monkify.util.AppLog
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel :
    ViewModel(),
    KoinComponent {
    private val firebaseAuth: FirebaseAuth by inject()
    private val log = AppLog.logger<MainViewModel>(level = Severity.Debug)
    private val _isAuthenticated = MutableStateFlow<Boolean?>(null)
    val isAuthenticated: StateFlow<Boolean?> = _isAuthenticated.asStateFlow()

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        viewModelScope.launch {
            _isAuthenticated.value = firebaseAuth.currentUser != null
            firebaseAuth.authStateChanged.collectLatest { user ->
                log.d { "User changed ${user?.uid}" }
                _isAuthenticated.value = user != null
            }
        }
    }
}
