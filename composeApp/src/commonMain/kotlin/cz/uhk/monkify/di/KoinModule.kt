package cz.uhk.monkify.di

import cz.uhk.monkify.database.remote.FirestoreTaskRepository
import cz.uhk.monkify.preferences.PreferencesManager
import cz.uhk.monkify.repository.DailyTaskRepository
import cz.uhk.monkify.screens.auth.AuthViewModel
import cz.uhk.monkify.screens.home.HomeViewModel
import cz.uhk.monkify.screens.plan.PlanViewModel
import cz.uhk.monkify.screens.task.TaskViewModel
import cz.uhk.monkify.service.StreakManager
import cz.uhk.monkify.viewmodel.MainViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::PlanViewModel)
    viewModelOf(::TaskViewModel)
    singleOf(::PreferencesManager)
    singleOf(::StreakManager)
    single<FirebaseAuth> { Firebase.auth }
    single<FirebaseFirestore> { Firebase.firestore }
    single { FirestoreTaskRepository(get()) }
    single { DailyTaskRepository(get(), get(), get()) }
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule, platformModule)
    }
}
