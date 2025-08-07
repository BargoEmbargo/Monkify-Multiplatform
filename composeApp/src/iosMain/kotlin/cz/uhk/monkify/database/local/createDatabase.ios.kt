package cz.uhk.monkify.database.local

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual fun createDatabase(context: Any): MonkifyMultiplatformDatabase {
    val docsUrl = NSFileManager.defaultManager.URLForDirectory(
        NSDocumentDirectory,
        NSUserDomainMask,
        null,
        false,
        null,
    ) ?: error("Couldn’t find documents directory")
    val dbFilePath = docsUrl.path + "/monkify.db"

    val builder: RoomDatabase.Builder<MonkifyMultiplatformDatabase> =
        Room.databaseBuilder<MonkifyMultiplatformDatabase>(
            name = dbFilePath,
        )

    return builder
        .setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(false)
        .build()
}
