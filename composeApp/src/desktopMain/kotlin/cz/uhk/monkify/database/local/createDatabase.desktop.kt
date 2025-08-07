package cz.uhk.monkify.database.local

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File
import kotlinx.coroutines.Dispatchers

actual fun createDatabase(context: Any): MonkifyMultiplatformDatabase {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "monkify.db").apply {
        parentFile?.mkdirs()
    }
    val dbPath = dbFile.absolutePath

    return Room.databaseBuilder<MonkifyMultiplatformDatabase>(name = dbPath)
        .setQueryCoroutineContext(Dispatchers.IO)
        .setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(false)
        .build()
}
