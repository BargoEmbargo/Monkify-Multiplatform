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
        val showBottomBar = currentDestination?.route in bottomBarRoutes
        val isAuthenticated by mainViewModel.isAuthenticated.collectAsState(initial = null)

        if (isAuthenticated == null) {
            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))
        } else {
            val startDestination = if (isAuthenticated == true) {
                NavigationGraph.HomeScreen.name
            } else {
                NavigationGraph.AuthScreen.name
            }

            MonkifyNavigation(
                navController = navController,
                showBottomBar = showBottomBar,
                startDestination = startDestination,
            )
        }
    }
}
