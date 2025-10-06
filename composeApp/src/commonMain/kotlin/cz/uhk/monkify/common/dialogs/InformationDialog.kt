package cz.uhk.monkify.common.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import cz.uhk.monkify.theme.MonkifyTheme
import monkifymultiplatform.composeapp.generated.resources.Res
import monkifymultiplatform.composeapp.generated.resources.task_checking_confirm
import monkifymultiplatform.composeapp.generated.resources.task_checking_message
import monkifymultiplatform.composeapp.generated.resources.task_checking_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun InformationDialog(
    title: String = stringResource(Res.string.task_checking_title),
    text: String = stringResource(Res.string.task_checking_message),
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = text) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.task_checking_confirm))
            }
        },
    )
}

@Preview
@Composable
private fun InformationDialogPreview() {
    MonkifyTheme {
        InformationDialog(onDismiss = {})
    }
}
