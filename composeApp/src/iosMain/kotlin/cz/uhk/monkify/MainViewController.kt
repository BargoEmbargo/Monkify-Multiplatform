package cz.uhk.monkify

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.ComposeUIViewController
import cz.uhk.monkify.ads.AdManager
import cz.uhk.monkify.di.initKoin
import org.koin.compose.koinInject

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    },
) {
    val adManager: AdManager = koinInject()
    
    // Initialize ads on iOS
    LaunchedEffect(Unit) {
        adManager.initialize()
    }
    
    App()
}
