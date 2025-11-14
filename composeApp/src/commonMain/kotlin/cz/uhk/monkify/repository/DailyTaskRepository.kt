package cz.uhk.monkify.repository

import co.touchlab.kermit.Severity
import cz.uhk.monkify.database.local.DailyTaskDao
import cz.uhk.monkify.database.model.DailyTask
import cz.uhk.monkify.database.remote.FirestoreTaskRepository
import cz.uhk.monkify.util.AppLog
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow

class DailyTaskRepository(
    private val dailyTask: DailyTaskDao,
    private val firestoreTaskRepository: FirestoreTaskRepository,
    private val firebaseAuth: FirebaseAuth,
) {
    private val log = AppLog.logger<DailyTaskRepository>(level = Severity.Debug)

    suspend fun upsertInfo(info: DailyTask) {
        log.d { "Upsert task for id=${info.id}" }
        dailyTask.upsertInfo(info)
        firebaseAuth.currentUser?.uid?.let { userId ->
            firestoreTaskRepository.uploadTask(userId, info)
        }
    }

    suspend fun deleteInfo(info: DailyTask) {
        log.d { "Delete task for id=${info.id}" }
        dailyTask.deleteInfo(info)
        firebaseAuth.currentUser?.uid?.let { userId ->
            firestoreTaskRepository.deleteTask(userId, info.id)
        }
    }

    suspend fun deleteAllInfo() {
        log.d { "Deleting ALL tasks" }
        dailyTask.deleteAllInfo()
        firebaseAuth.currentUser?.uid?.let { userId ->
            firestoreTaskRepository.deleteAllTasks(userId)
        }
    }

    fun getArticles(): Flow<List<DailyTask>> {
        log.d { "Getting all articles" }
        return dailyTask.getArticles()
    }

    suspend fun getInfoById(id: Int): DailyTask {
        log.d { "Get task by id=$id" }
        return dailyTask.getInfoById(id = id)
    }
}
