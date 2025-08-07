package cz.uhk.monkify.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cz.uhk.monkify.database.local.DailyTaskDao
import cz.uhk.monkify.database.local.MonkifyMultiplatformDatabase
import cz.uhk.monkify.database.local.createDatabase
import cz.uhk.monkify.preferences.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<DataStore<Preferences>> {
        createDataStore()
    }

    single<MonkifyMultiplatformDatabase> {
        createDatabase(Any())
    }

    single<DailyTaskDao> {
        get<MonkifyMultiplatformDatabase>().dailyTaskDao()
    }
}
