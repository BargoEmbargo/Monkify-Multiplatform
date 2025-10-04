package cz.uhk.monkify.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cz.uhk.monkify.ModalDrawerSheetContent
import cz.uhk.monkify.common.MenuTopBar
import cz.uhk.monkify.navigation.bottombar.ui.MonkifyBottomBar
import cz.uhk.monkify.screens.auth.AuthScreen
import cz.uhk.monkify.screens.auth.SignInScreen
import cz.uhk.monkify.screens.auth.SignUpScreen
import cz.uhk.monkify.screens.home.HomeScreen
import cz.uhk.monkify.screens.onboarding.OnboardingScreen
import cz.uhk.monkify.screens.plan.PlanScreen
import cz.uhk.monkify.screens.task.TaskScreen
import kotlinx.coroutines.launch

@Composable
fun MonkifyNavigation(
    navController: NavHostController,
    showNavigationBars: Boolean,
    startDestination: String,
    onOnboardingFinish: () -> Unit,
    onLogout: () -> Unit = {},
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { ModalDrawerSheetContent(onClick = onLogout) },
    ) {
        Scaffold(
            bottomBar = {
                if (showNavigationBars) {
                    MonkifyBottomBar(navController)
                }
            },
            topBar = {
                if (showNavigationBars) {
                    MenuTopBar(onClick = { scope.launch { drawerState.open() } })
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(NavigationGraph.HomeScreen.name) { HomeScreen(navController) }
                composable(NavigationGraph.PlanScreen.name) { PlanScreen(navController) }
                composable(NavigationGraph.AuthScreen.name) {
                    AuthScreen(
                        navController = navController,
                        onSignInClick = { navController.navigate(NavigationGraph.SignInScreen.name) },
                        onSignUpClick = { navController.navigate(NavigationGraph.SignUpScreen.name) },
                    )
                }
                composable(NavigationGraph.SignInScreen.name) {
                    SignInScreen(
                        navController = navController,
                        onSuccess = {
                            navController.navigate(NavigationGraph.HomeScreen.name) {
                                popUpTo(NavigationGraph.AuthScreen.name) { inclusive = true }
                            }
                        },
                    )
                }
                composable(NavigationGraph.SignUpScreen.name) {
                    SignUpScreen(
                        navController = navController,
                        onSuccess = {
                            navController.navigate(NavigationGraph.HomeScreen.name) {
                                popUpTo(NavigationGraph.AuthScreen.name) { inclusive = true }
                            }
                        },
                    )
                }
                composable(NavigationGraph.OnboardingScreen.name) {
                    OnboardingScreen(
                        onFinish = { onOnboardingFinish() },
                    )
                }
                composable(
                    route = NavigationGraph.TaskScreen.name + "?taskId={taskId}",
                    arguments = listOf(
                        navArgument("taskId") {
                            type = NavType.IntType
                            defaultValue = -1
                        },
                    ),
                ) { backStackEntry ->
                    val taskId = backStackEntry.savedStateHandle.get<Int>("taskId") ?: -1
                    TaskScreen(navController, taskId)
                }
            }
        }
    }
}
