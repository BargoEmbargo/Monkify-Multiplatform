package cz.uhk.monkify.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Preview
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = koinViewModel()) {}
