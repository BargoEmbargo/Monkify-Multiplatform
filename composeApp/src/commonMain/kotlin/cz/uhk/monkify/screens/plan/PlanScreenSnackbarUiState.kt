package cz.uhk.monkify.screens.plan

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import monkifymultiplatform.composeapp.generated.resources.Res
import monkifymultiplatform.composeapp.generated.resources.all_task_completed_message
import monkifymultiplatform.composeapp.generated.resources.deny_setup_message
import org.jetbrains.compose.resources.stringResource

/**
 * Holds all snackbar-related state and messages forr the plan screen.
 */
data class PlanScreenSnackbarUiState(
    val snackbarHostState: SnackbarHostState,
    val setupBlockedSnackbar: MutableState<Boolean>,
    val allTaskCompletedMessage: String,
    val setupBlockedMessage: String,
)

@Composable
fun rememberSnackbarUiState(): PlanScreenSnackbarUiState {
    val snackbarHostState = remember { SnackbarHostState() }
    val setupBlockedSnackbar = remember { mutableStateOf(false) }
    val allTaskCompletedMessage = stringResource(Res.string.all_task_completed_message)
    val setupBlockedMessage = stringResource(Res.string.deny_setup_message)
    return PlanScreenSnackbarUiState(
        snackbarHostState = snackbarHostState,
        setupBlockedSnackbar = setupBlockedSnackbar,
        allTaskCompletedMessage = allTaskCompletedMessage,
        setupBlockedMessage = setupBlockedMessage,
    )
}
