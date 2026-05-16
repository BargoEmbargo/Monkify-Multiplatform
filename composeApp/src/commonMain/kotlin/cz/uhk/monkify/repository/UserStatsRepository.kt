package cz.uhk.monkify.repository

import co.touchlab.kermit.Severity
import com.kizitonwose.calendar.core.now
import cz.uhk.monkify.database.model.UserStats
import cz.uhk.monkify.database.remote.FirestoreTaskRepository
import cz.uhk.monkify.preferences.PreferencesManager
import cz.uhk.monkify.util.AppLog
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalTime::class)
class UserStatsRepository(
    private val preferencesManager: PreferencesManager,
    private val firestoreTaskRepository: FirestoreTaskRepository,
    private val firebaseAuth: FirebaseAuth,
) {
    private val log = AppLog.logger<UserStatsRepository>(level = Severity.Debug)

    suspend fun setDaysCompleted(value: Int) {
        preferencesManager.setValue(PreferencesManager.DAYS_COMPLETED, value)
        syncToRemote()
    }

    suspend fun setLastActivityDate(value: String) {
        preferencesManager.setValue(PreferencesManager.LAST_ACTIVITY_DATE, value)
        syncToRemote()
    }

    suspend fun syncToRemote() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val days = preferencesManager.getValue(PreferencesManager.DAYS_COMPLETED, 0).first()
        val lastActivity = preferencesManager.getValue(PreferencesManager.LAST_ACTIVITY_DATE, "").first()
        try {
            firestoreTaskRepository.uploadStats(userId, UserStats(days, lastActivity))
        } catch (e: Exception) {
            log.e(e) { "Failed to sync user stats to remote" }
        }
    }

    suspend fun pullFromRemote() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        val stats = try {
            firestoreTaskRepository.getStats(userId)
        } catch (e: Exception) {
            log.e(e) { "Failed to pull user stats from remote" }
            null
        }
        if (stats == null) {
            log.i { "No remote stats found, pushing local stats to remote" }
            syncToRemote()
            return
        }
        preferencesManager.setValue(PreferencesManager.DAYS_COMPLETED, stats.daysCompleted)
        preferencesManager.setValue(PreferencesManager.LAST_ACTIVITY_DATE, LocalDate.now().toString())
    }
}
