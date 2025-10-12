package cz.uhk.monkify.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cz.uhk.monkify.theme.MonkifyTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PrimaryOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector? = null,
    contentDescription: String? = null,
    iconPadding: Dp = 8.dp,
    height: Dp = 56.dp,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
    containerColor: Color = Color.Transparent,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    disabledContentColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    loading: Boolean = false,
    contentAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
) {
    val borderColor = if (enabled) contentColor else disabledContentColor
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(height),
        border = ButtonDefaults.outlinedButtonBorder(enabled = enabled).copy(
            brush = SolidColor(borderColor),
        ),
        shape = shape,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = borderColor,
        ),
        enabled = enabled && !loading,
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = borderColor,
                strokeWidth = 2.dp,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(24.dp),
            )
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = when (contentAlignment) {
                    Alignment.Start -> Arrangement.Start
                    Alignment.CenterHorizontally -> Arrangement.Center
                    Alignment.End -> Arrangement.End
                    else -> Arrangement.Center
                },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = contentDescription,
                        modifier = Modifier.padding(end = iconPadding),
                    )
                }
                Text(text, style = textStyle)
            }
        }
    }
}

@Preview
@Composable
private fun PrimaryOutlinedButtonPreview() {
    MonkifyTheme {
        PrimaryOutlinedButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            text = "Click Me",
        )
    }
}

@Preview
@Composable
private fun PrimaryOutlinedButtonWithIconPreview() {
    MonkifyTheme {
        PrimaryOutlinedButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            text = "Add Item",
            icon = Icons.Default.Add,
        )
    }
}
