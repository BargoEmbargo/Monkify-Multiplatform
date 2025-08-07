package cz.uhk.monkify.database.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import cz.uhk.monkify.database.model.DailyTask

@Database(
    entities = [DailyTask::class],
    version = 1,
)
@ConstructedBy(MonkifyDatabaseConstructor::class)
abstract class MonkifyMultiplatformDatabase : RoomDatabase() {
    abstract fun dailyTaskDao(): DailyTaskDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object MonkifyDatabaseConstructor : RoomDatabaseConstructor<MonkifyMultiplatformDatabase> {
    override fun initialize(): MonkifyMultiplatformDatabase
}
