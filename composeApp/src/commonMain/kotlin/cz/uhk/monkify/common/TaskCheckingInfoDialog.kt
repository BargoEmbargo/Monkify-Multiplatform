package cz.uhk.monkify.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import monkifymultiplatform.composeapp.generated.resources.Res
import monkifymultiplatform.composeapp.generated.resources.task_checking_confirm
import monkifymultiplatform.composeapp.generated.resources.task_checking_message
import monkifymultiplatform.composeapp.generated.resources.task_checking_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskCheckingInfoDialog(
    show: Boolean,
    onDismiss: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = stringResource(Res.string.task_checking_title)) },
            text = { Text(text = stringResource(Res.string.task_checking_message)) },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(Res.string.task_checking_confirm))
                }
            }
        )
    }
}
