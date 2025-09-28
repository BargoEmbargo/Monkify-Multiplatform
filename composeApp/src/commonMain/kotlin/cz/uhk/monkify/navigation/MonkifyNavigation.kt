package cz.uhk.monkify.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cz.uhk.monkify.navigation.bottombar.ui.MonkifyBottomBar
import cz.uhk.monkify.screens.auth.AuthScreen
import cz.uhk.monkify.screens.auth.SignInScreen
import cz.uhk.monkify.screens.auth.SignUpScreen
import cz.uhk.monkify.screens.home.HomeScreen
import cz.uhk.monkify.screens.onboarding.OnboardingScreen
import cz.uhk.monkify.screens.plan.PlanScreen

@Composable
fun MonkifyNavigation(
    navController: NavHostController,
    showBottomBar: Boolean,
    startDestination: String,
    onOnboardingFinish: () -> Unit,
) {
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                MonkifyBottomBar(navController)
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
        }
    }
}
