package cz.uhk.monkify.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cz.uhk.monkify.theme.MonkifyTheme
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun GlassmorpismCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val borderColors = remember { listOf(Color.White.copy(alpha = 0.2f), Color.White.copy(alpha = 0.85f)) }
    val animationDuration = 2000
    var currentColorIndex by remember { mutableStateOf(0) }

    val color = animateColorAsState(
        targetValue = borderColors[currentColorIndex % borderColors.size],
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
    ).value

    LaunchedEffect(currentColorIndex) {
        delay(animationDuration.toLong())
        currentColorIndex = (currentColorIndex + 1) % borderColors.size
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .border(
                2.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(color, color),
                    startY = 0.0f,
                    endY = 400.0f,
                ),
                shape = MaterialTheme.shapes.large,
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.3f),
                        Color.White.copy(alpha = 0.35f),
                    ),
                    startY = 100.0f,
                    endY = 400.0f,
                ),
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        content()
    }
}

@Preview
@Composable
private fun GlassmorpismCardPreview() {
    MonkifyTheme {
        GlassmorpismCard {
            Box(modifier = Modifier.size(200.dp)){
                Text("Glassmorphism Card", modifier = Modifier.padding(16.dp))
            }
        }
    }
}
