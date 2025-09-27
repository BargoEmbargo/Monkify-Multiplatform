package cz.uhk.monkify.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
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
import androidx.navigation.NavController
import cz.uhk.monkify.common.GlassmorpismCard
import cz.uhk.monkify.common.PasswordTextField
import cz.uhk.monkify.theme.MonkifyTheme
import cz.uhk.monkify.wrapper.ScreenContentWrapper
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInScreen(
    navController: NavController,
    onSuccess: () -> Unit = {},
    viewModel: AuthViewModel = koinViewModel(),
) {
    var formState by remember { mutableStateOf(AuthFormState()) }
    var localError by remember { mutableStateOf<String?>(null) }
    val uiState by viewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

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
                localError = "Email and password must not be empty"
            } else {
                localError = null
                viewModel.signIn(formState.email, formState.password)
            }
        },
        passwordVisible = passwordVisible,
        onPasswordVisibilityChange = { passwordVisible = it }
    )
}

@Composable
fun SignInScreenContent(
    formState: AuthFormState,
    onFormChange: (AuthFormState) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onSignInClick: () -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
) {
    ScreenContentWrapper(
        showScrollbar = false,
        contentAlignment = Alignment.Center,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            "Sign In",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start),
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
                            Text("Email")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                )
                PasswordTextField(
                    value = formState.password,
                    onValueChange = { onFormChange(formState.copy(password = it)) },
                    label = "Password",
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityChange = onPasswordVisibilityChange,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        if (errorMessage != null) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onSignInClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
        ) {
            Text(if (isLoading) "Signing In..." else "Sign In")
        }
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
            passwordVisible = false,
            onPasswordVisibilityChange = {}
        )
    }
}
