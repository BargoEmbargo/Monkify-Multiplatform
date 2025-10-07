package cz.uhk.monkify.connectivity

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SignalWifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.uhk.monkify.common.PrimaryOutlinedButton
import cz.uhk.monkify.theme.MonkifyTheme
import cz.uhk.monkify.wrapper.ScreenContentWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import monkifymultiplatform.composeapp.generated.resources.Res
import monkifymultiplatform.composeapp.generated.resources.check_internet_connection
import monkifymultiplatform.composeapp.generated.resources.no_internet_connection
import monkifymultiplatform.composeapp.generated.resources.try_again
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OfflineScreen(onTryAgain: () -> Unit) {
    var loading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    ScreenContentWrapper {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.align(Alignment.Center)) {
                Icon(
                    imageVector = Icons.Filled.SignalWifiOff,
                    contentDescription = Icons.Filled.SignalWifiOff.name,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .align(Alignment.CenterHorizontally),
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(Res.string.no_internet_connection),
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(Res.string.check_internet_connection),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            PrimaryOutlinedButton(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                onClick = {
                    loading = true
                    coroutineScope.launch {
                        delay(1500) // fake delay for better UX
                        loading = false
                        onTryAgain()
                    }
                },
                icon = Icons.Default.Refresh,
                text = stringResource(Res.string.try_again),
                loading = loading,
            )
        }
    }
}

@Preview
@Composable
private fun OfflineScreenPreview() {
    MonkifyTheme {
        OfflineScreen(onTryAgain = {})
    }
}
