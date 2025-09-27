package cz.uhk.monkify.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cz.uhk.monkify.theme.MonkifyTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Icon(Icons.Filled.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(label)
            }
        },
        modifier = modifier,
        singleLine = true,
        enabled = enabled,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { onPasswordVisibilityChange(!passwordVisible) }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                )
            }
        },
    )
}

@Preview
@Composable
private fun PasswordTextFieldPreview() {
    MonkifyTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            PasswordTextField(
                value = "",
                onValueChange = { },
                label = "Password",
                passwordVisible = false,
                onPasswordVisibilityChange = { },
                enabled = true,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
    }
}

@Preview
@Composable
private fun PasswordTextFieldWithHiddenValuePreview() {
    MonkifyTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            PasswordTextField(
                value = "Password",
                onValueChange = { },
                label = "Password",
                passwordVisible = false,
                onPasswordVisibilityChange = { },
                enabled = true,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
    }
}

@Preview
@Composable
private fun PasswordTextFieldWithValuePreview() {
    MonkifyTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            PasswordTextField(
                value = "Password",
                onValueChange = { },
                label = "Password",
                passwordVisible = true,
                onPasswordVisibilityChange = { },
                enabled = true,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
    }
}
