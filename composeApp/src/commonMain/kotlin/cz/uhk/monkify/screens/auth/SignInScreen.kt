package cz.uhk.monkify.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.uhk.monkify.common.BackTopBar
import cz.uhk.monkify.common.GlassmorpismCard
import cz.uhk.monkify.common.PasswordTextField
import cz.uhk.monkify.common.PrimaryOutlinedButton
import cz.uhk.monkify.theme.MonkifyTheme
import cz.uhk.monkify.wrapper.ScreenContentWrapper
import monkifymultiplatform.composeapp.generated.resources.Res
import monkifymultiplatform.composeapp.generated.resources.email
import monkifymultiplatform.composeapp.generated.resources.email_and_password_empty
import monkifymultiplatform.composeapp.generated.resources.password
import monkifymultiplatform.composeapp.generated.resources.sign_in
import monkifymultiplatform.composeapp.generated.resources.sign_in_loading
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInScreen(
    onSuccess: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    viewModel: AuthViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var formState by remember { mutableStateOf(AuthFormState()) }
    var localError by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }
    val localErrorMessage = stringResource(Res.string.email_and_password_empty)

    // If sign-in is successful, call onSuccess
    if (uiState.isSuccess) {
        onSuccess()
        viewModel.clearState()
    }

    SignInScreenContent(
        formState = formState,
        onFormChange = { formState = it },
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage ?: localError,
        onSignInClick = {
            if (formState.email.isBlank() || formState.password.isBlank()) {
                localError = localErrorMessage
            } else {
                localError = null
                viewModel.signIn(formState.email, formState.password)
            }
        },
        onNavigateBack = onNavigateBack,
        passwordVisible = passwordVisible,
        onPasswordVisibilityChange = { passwordVisible = it },
    )
}

@Composable
private fun SignInScreenContent(
    formState: AuthFormState,
    onFormChange: (AuthFormState) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onSignInClick: () -> Unit,
    onNavigateBack: () -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        ScreenContentWrapper(
            modifier = Modifier.imePadding(),
            showScrollbar = false,
            contentAlignment = Alignment.Center,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(Res.string.sign_in),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth().align(Alignment.Start),
            )
            Spacer(modifier = Modifier.height(16.dp))
            GlassmorpismCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    OutlinedTextField(
                        value = formState.email,
                        onValueChange = { onFormChange(formState.copy(email = it)) },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Email, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(stringResource(Res.string.email))
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,
                    )
                    PasswordTextField(
                        value = formState.password,
                        onValueChange = { onFormChange(formState.copy(password = it)) },
                        label = stringResource(Res.string.password),
                        passwordVisible = passwordVisible,
                        onPasswordVisibilityChange = onPasswordVisibilityChange,
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
            if (errorMessage != null) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.weight(1f))
            PrimaryOutlinedButton(
                onClick = onSignInClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                containerColor = MaterialTheme.colorScheme.primary.copy(0.2f),
                text = stringResource(if (isLoading) Res.string.sign_in_loading else Res.string.sign_in),
            )
        }

        BackTopBar(
            onNavigateBack = onNavigateBack,
            modifier = Modifier.align(Alignment.TopStart),
        )
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    MonkifyTheme {
        SignInScreenContent(
            formState = AuthFormState(),
            onFormChange = {},
            isLoading = false,
            errorMessage = null,
            onSignInClick = {},
            onNavigateBack = {},
            passwordVisible = false,
            onPasswordVisibilityChange = {},
        )
    }
}
