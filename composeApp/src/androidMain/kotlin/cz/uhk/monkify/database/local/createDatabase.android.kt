package cz.uhk.monkify.database.local

import android.content.Context
import androidx.room.Room

actual fun createDatabase(context: Any): MonkifyMultiplatformDatabase {
    val appContext = (context as Context).applicationContext
    return Room.databaseBuilder(
        appContext,
        MonkifyMultiplatformDatabase::class.java,
        appContext.getDatabasePath("monkify.db").absolutePath,
    )
        .fallbackToDestructiveMigration(false)
        .build()
}
