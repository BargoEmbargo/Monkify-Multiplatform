package cz.uhk.monkify.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.uhk.monkify.screens.HomeScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.name,
    ) {
        composable(Screen.Home.name) {
            HomeScreen(navController)
        }
    }
}
