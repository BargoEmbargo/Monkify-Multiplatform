package cz.uhk.monkify

import SplashOverlay
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cz.uhk.monkify.navigation.MonkifyNavigation
import cz.uhk.monkify.navigation.NavigationGraph
import cz.uhk.monkify.theme.MonkifyTheme
import cz.uhk.monkify.util.Constants.SPLASH_SCREEN_DELAY
import cz.uhk.monkify.viewmodel.MainViewModel
import kotlinx.coroutines.delay
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

        var minSplashElapsed by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            delay(SPLASH_SCREEN_DELAY)
            minSplashElapsed = true
        }

        val resolvedFlags = onboardingCompleted != null && isAuthenticated != null
        val showSplash = !minSplashElapsed || !resolvedFlags

        val startDestination = when {
            !resolvedFlags -> NavigationGraph.PlanScreen.name
            onboardingCompleted == false -> NavigationGraph.OnboardingScreen.name
            isAuthenticated == true -> NavigationGraph.HomeScreen.name
            else -> NavigationGraph.AuthScreen.name
        }

        Box(Modifier.fillMaxSize()) {
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
            SplashOverlay(showSplash)
        }
    }
}
