package cz.uhk.monkify.di

import cz.uhk.monkify.preferences.PreferencesManager
import cz.uhk.monkify.repository.DailyTaskRepository
import cz.uhk.monkify.screens.home.HomeViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    viewModelOf(::HomeViewModel)
    singleOf(::PreferencesManager)
    single { DailyTaskRepository(get()) }
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule, platformModule)
    }
}
