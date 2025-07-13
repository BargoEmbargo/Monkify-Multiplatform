package cz.uhk.monkify

import androidx.compose.ui.window.ComposeUIViewController
import cz.uhk.monkify.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    },
) {
    App()
}
