package cz.uhk.monkify.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.uhk.monkify.navigation.bottombar.ui.MonkifyBottomBar
import cz.uhk.monkify.screens.home.HomeScreen
import cz.uhk.monkify.screens.plan.PlanScreen

@Composable
fun MonkifyNavigation() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { MonkifyBottomBar(navController) },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationGraph.HomeScreen.name,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(NavigationGraph.HomeScreen.name) { HomeScreen(navController) }
            composable(NavigationGraph.PlanScreen.name) { PlanScreen(navController) }
        }
    }
}
