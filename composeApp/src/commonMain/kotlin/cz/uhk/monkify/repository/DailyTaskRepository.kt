package cz.uhk.monkify.repository

import cz.uhk.monkify.database.local.DailyTaskDao
import cz.uhk.monkify.database.model.DailyTask
import kotlinx.coroutines.flow.Flow

class DailyTaskRepository(private val dailyTask: DailyTaskDao) {
    suspend fun upsertInfo(info: DailyTask) {
        dailyTask.upsertInfo(info)
    }

    suspend fun deleteInfo(info: DailyTask) {
        dailyTask.deleteInfo(info)
    }

    suspend fun deleteAllInfo() {
        dailyTask.deleteAllInfo()
    }

    fun getArticles(): Flow<List<DailyTask>> = dailyTask.getArticles()

    suspend fun getInfoById(id: Int): DailyTask = dailyTask.getInfoById(id = id)
}
