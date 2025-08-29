package cz.uhk.monkify.repository

import co.touchlab.kermit.Severity
import cz.uhk.monkify.database.local.DailyTaskDao
import cz.uhk.monkify.database.model.DailyTask
import cz.uhk.monkify.util.AppLog
import kotlinx.coroutines.flow.Flow

class DailyTaskRepository(private val dailyTask: DailyTaskDao) {
    private val log = AppLog.logger<DailyTaskRepository>(level = Severity.Info)

    suspend fun upsertInfo(info: DailyTask) {
        log.d { "Upsert task for id=${info.id}" }
        dailyTask.upsertInfo(info)
    }

    suspend fun deleteInfo(info: DailyTask) {
        log.d { "Delete task for id=${info.id}" }
        dailyTask.deleteInfo(info)
    }

    suspend fun deleteAllInfo() {
        log.d { "Deleting ALL tasks" }
        dailyTask.deleteAllInfo()
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
