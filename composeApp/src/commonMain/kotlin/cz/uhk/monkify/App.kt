package cz.uhk.monkify

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cz.uhk.monkify.navigation.NavGraph

@Composable
fun App() {
    MaterialTheme {
        NavGraph()
    }
}
