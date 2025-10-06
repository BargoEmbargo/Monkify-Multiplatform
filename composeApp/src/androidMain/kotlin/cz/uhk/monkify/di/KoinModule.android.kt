package cz.uhk.monkify.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cz.uhk.monkify.ads.AdManager
import cz.uhk.monkify.ads.createAdManager
import cz.uhk.monkify.database.local.DailyTaskDao
import cz.uhk.monkify.database.local.MonkifyMultiplatformDatabase
import cz.uhk.monkify.database.local.createDatabase
import cz.uhk.monkify.preferences.createDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<DataStore<Preferences>> { createDataStore(androidContext()) }

    single<MonkifyMultiplatformDatabase> {
        createDatabase(androidContext())
    }

    single<DailyTaskDao> {
        get<MonkifyMultiplatformDatabase>().dailyTaskDao()
    }
    
    single<AdManager> {
        createAdManager()
    }
}
