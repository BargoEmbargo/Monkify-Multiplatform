package cz.uhk.monkify

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.uhk.monkify.common.PrimaryOutlinedButton
import cz.uhk.monkify.common.dialogs.DeleteConfirmationDialog
import cz.uhk.monkify.theme.MonkifyTheme
import cz.uhk.monkify.util.Constants
import monkifymultiplatform.composeapp.generated.resources.Res
import monkifymultiplatform.composeapp.generated.resources.app_name
import monkifymultiplatform.composeapp.generated.resources.app_version
import monkifymultiplatform.composeapp.generated.resources.log_out
import monkifymultiplatform.composeapp.generated.resources.reset_progress_message
import monkifymultiplatform.composeapp.generated.resources.reset_progress_title
import monkifymultiplatform.composeapp.generated.resources.restart_progress
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ModalDrawerSheetContent(
    onLogout: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    ModalDrawerSheet(modifier = modifier) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            // todo refactor this
            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.titleLarge,
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp).fillMaxWidth(0.65f))
            ResetOutlinedButton(onClick = { showDeleteDialog = true })
            Spacer(modifier = Modifier.height(24.dp))
            LogoutOutlinedButton(onClick = onLogout)
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(0.65f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(imageVector = Icons.Default.Info, contentDescription = Icons.Default.Info.name)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = stringResource(Res.string.app_version))
                    Text(
                        text = Constants.APP_VERSION,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }

    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            onConfirm = {
                onReset.invoke()
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false },
            titleText = stringResource(Res.string.reset_progress_title),
            messageText = stringResource(Res.string.reset_progress_message),
            confirmButtonText = stringResource(Res.string.restart_progress),
        )
    }
}

@Composable
private fun LogoutOutlinedButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    PrimaryOutlinedButton(
        onClick = onClick,
        height = 42.dp,
        contentAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth(0.65f),
        text = stringResource(Res.string.log_out),
        icon = Icons.AutoMirrored.Filled.Logout,
        contentDescription = Icons.AutoMirrored.Filled.Logout.name,
    )
}

@Composable
private fun ResetOutlinedButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    PrimaryOutlinedButton(
        contentColor = MaterialTheme.colorScheme.error,
        onClick = onClick,
        height = 42.dp,
        contentAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth(0.65f),
        text = stringResource(Res.string.reset_progress_title),
        icon = Icons.Default.Restore,
        contentDescription = Icons.Default.Restore.name,
    )
}

@Preview
@Composable
private fun ModalDrawerSheetContentPreview() {
    MonkifyTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            ModalDrawerSheetContent(onLogout = {}, onReset = {})
        }
    }
}
