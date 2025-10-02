package cz.uhk.monkify.common.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import cz.uhk.monkify.theme.MonkifyTheme
import monkifymultiplatform.composeapp.generated.resources.Res
import monkifymultiplatform.composeapp.generated.resources.delete_all_tasks_message
import monkifymultiplatform.composeapp.generated.resources.delete_all_tasks_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DeleteConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(Res.string.delete_all_tasks_title)) },
        text = { Text(text = stringResource(Res.string.delete_all_tasks_message)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    "Delete",
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}

@Preview
@Composable
private fun DeleteConfirmationDialogPreview() {
    MonkifyTheme {
        DeleteConfirmationDialog(onConfirm = {}, onDismiss = {})
    }
}
