package cz.uhk.monkify

import androidx.compose.runtime.Composable
import cz.uhk.monkify.navigation.MonkifyNavigation
import cz.uhk.monkify.theme.MonkifyTheme

@Composable
fun App() {
    MonkifyTheme {
        MonkifyNavigation()
    }
}
