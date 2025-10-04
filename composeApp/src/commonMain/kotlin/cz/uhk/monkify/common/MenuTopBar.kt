package cz.uhk.monkify.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Menu
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
fun MenuTopBar(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().statusBarsPadding()
            .applyHorizontalScreenPadding(ScreenHorizontalPaddingClass.Half),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
    ) {
        IconButton(onClick = onClick) {
            Icon(Icons.Default.Menu, contentDescription = Icons.Default.Menu.name)
        }
    }
}

@Preview
@Composable
private fun MenuTopBarPreview() {
    MonkifyTheme {
        MenuTopBar(onClick = {}, modifier = Modifier.background(MaterialTheme.colorScheme.background))
    }
}
