package cz.uhk.monkify.database.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import cz.uhk.monkify.database.model.DailyTask
import cz.uhk.monkify.util.Constants.DATABASE_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyTaskDao {
    @Upsert()
    suspend fun upsertInfo(info: DailyTask)

    @Delete
    suspend fun deleteInfo(info: DailyTask)

    @Query("SELECT * FROM $DATABASE_TABLE_NAME")
    fun getArticles(): Flow<List<DailyTask>>

    @Query("DELETE FROM $DATABASE_TABLE_NAME")
    suspend fun deleteAllInfo()

    @Query("SELECT * FROM $DATABASE_TABLE_NAME WHERE id=:id")
    suspend fun getInfoById(id: Int): DailyTask
}
