package cz.uhk.monkify.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.uhk.monkify.common.PrimaryOutlinedButton
import cz.uhk.monkify.theme.MonkifyTheme
import cz.uhk.monkify.wrapper.ScreenContentWrapper
import monkifymultiplatform.composeapp.generated.resources.Res
import monkifymultiplatform.composeapp.generated.resources.google
import monkifymultiplatform.composeapp.generated.resources.sign_in
import monkifymultiplatform.composeapp.generated.resources.sign_up
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AuthScreen(onSignInClick: () -> Unit = {}, onSignUpClick: () -> Unit = {}) {
    AuthScreenContent(onSignInClick, onSignUpClick)
}

@Composable
fun AuthScreenContent(onSignInClick: () -> Unit, onSignUpClick: () -> Unit) {
    ScreenContentWrapper(
        showScrollbar = false,
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = painterResource(resource = Res.drawable.google),
                    contentDescription = "App Icon",
                )
                Text(
                    text = "Welcome!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "Choose how to continue",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                PrimaryOutlinedButton(
                    onClick = onSignInClick,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.primary.copy(0.2f),
                    text = stringResource(Res.string.sign_in),
                )
                PrimaryOutlinedButton(
                    onClick = onSignUpClick,
                    modifier = Modifier.fillMaxWidth(),
                    contentColor = MaterialTheme.colorScheme.secondary,
                    containerColor = MaterialTheme.colorScheme.secondary.copy(0.2f),
                    text = stringResource(Res.string.sign_up),
                )
            }
        }
    }
}

@Preview
@Composable
private fun AuthScreenPreview() {
    MonkifyTheme {
        AuthScreenContent(onSignInClick = {}, onSignUpClick = {})
    }
}
