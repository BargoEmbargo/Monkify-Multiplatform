package cz.uhk.monkify

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cz.uhk.monkify.navigation.MonkifyNavigation
import cz.uhk.monkify.navigation.NavigationGraph
import cz.uhk.monkify.theme.MonkifyTheme
import cz.uhk.monkify.viewmodel.MainViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    MonkifyTheme {
        val navController = rememberNavController()
        val mainViewModel: MainViewModel = koinViewModel()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val bottomBarRoutes = listOf(
            NavigationGraph.HomeScreen.name,
            NavigationGraph.PlanScreen.name,
        )
        val showNavigationBars = currentDestination?.route in bottomBarRoutes
        val isAuthenticated by mainViewModel.isAuthenticated.collectAsState(initial = null)
        val onboardingCompleted by mainViewModel.onboardingCompleted.collectAsState(initial = null)

        if (onboardingCompleted == null || isAuthenticated == null) {
            Box(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            val startDestination = when {
                onboardingCompleted == false -> NavigationGraph.OnboardingScreen.name
                isAuthenticated == true -> NavigationGraph.HomeScreen.name
                else -> NavigationGraph.AuthScreen.name
            }

            MonkifyNavigation(
                navController = navController,
                showNavigationBars = showNavigationBars,
                startDestination = startDestination,
                onOnboardingFinish = {
                    mainViewModel.setOnboardingCompleted()
                    if (mainViewModel.isAuthenticated.value == true) {
                        navController.navigate(NavigationGraph.HomeScreen.name) {
                            popUpTo(NavigationGraph.OnboardingScreen.name) { inclusive = true }
                        }
                    } else {
                        navController.navigate(NavigationGraph.AuthScreen.name)
                    }
                },
                onLogout = {
                    mainViewModel.logout()
                    navController.navigate(NavigationGraph.AuthScreen.name) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onReset = {
                    mainViewModel.resetProgress()
                },
            )
        }
    }
}
