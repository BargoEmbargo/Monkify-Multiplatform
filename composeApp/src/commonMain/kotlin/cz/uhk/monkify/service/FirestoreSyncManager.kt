package cz.uhk.monkify.service

import co.touchlab.kermit.Severity
import cz.uhk.monkify.database.local.DailyTaskDao
import cz.uhk.monkify.database.remote.FirestoreTaskRepository
import cz.uhk.monkify.preferences.PreferencesManager
import cz.uhk.monkify.util.AppLog
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FirestoreSyncManager(
    private val firebaseAuth: FirebaseAuth,
    private val firestoreTaskRepository: FirestoreTaskRepository,
    private val dailyTaskDao: DailyTaskDao,
    private val preferencesManager: PreferencesManager,
) {
    private val log = AppLog.logger<FirestoreSyncManager>(level = Severity.Info)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var syncJob: Job? = null
    private var started = false

    fun start() {
        if (started) return
        started = true
        scope.launch {
            firebaseAuth.authStateChanged.collectLatest { user ->
                syncJob?.cancel()
                if (user != null) {
                    log.i { "Starting Firestore sync for uid=${user.uid}" }
                    syncJob = scope.launch {
                        launch { syncTasks(user.uid) }
                        launch { syncStats(user.uid) }
                    }
                } else {
                    log.i { "User signed out, sync stopped" }
                }
            }
        }
    }

    private suspend fun syncTasks(userId: String) {
        firestoreTaskRepository.observeTasks(userId).collectLatest { remoteTasks ->
            log.d { "Remote tasks changed: ${remoteTasks.size} tasks" }
            dailyTaskDao.deleteAllInfo()
            remoteTasks.forEach { dailyTaskDao.upsertInfo(it) }
        }
    }

    private suspend fun syncStats(userId: String) {
        firestoreTaskRepository.observeStats(userId).collectLatest { stats ->
            if (stats != null) {
                log.d { "Remote stats changed: daysCompleted=${stats.daysCompleted}" }
                preferencesManager.setValue(PreferencesManager.DAYS_COMPLETED, stats.daysCompleted)
            }
        }
    }
}
