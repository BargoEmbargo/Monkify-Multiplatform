package cz.uhk.monkify.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cz.uhk.monkify.extension.applyHorizontalScreenPadding
import cz.uhk.monkify.theme.MonkifyTheme
import cz.uhk.monkify.wrapper.ScreenHorizontalPaddingClass
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BackTopBar(onNavigateBack: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth()
            .applyHorizontalScreenPadding(ScreenHorizontalPaddingClass.Half),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onNavigateBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
    }
}

@Preview
@Composable
private fun BackTopBarPreview() {
    MonkifyTheme {
        BackTopBar(onNavigateBack = {}, modifier = Modifier.background(MaterialTheme.colorScheme.background))
    }
}
