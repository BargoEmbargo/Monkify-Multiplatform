package cz.uhk.monkify.common

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cz.uhk.monkify.theme.MonkifyTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PieChart(
    data: Map<String, Int>,
    modifier: Modifier = Modifier,
    radiusOuter: Dp = 48.dp,
    chartBarWidth: Dp = 13.dp,
    animDuration: Int = 1000,
    primaryColor: Color = MaterialTheme.colorScheme.primaryContainer,
    secondaryColor: Color = MaterialTheme.colorScheme.secondary,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    progressOverride: Float? = null, // Added this for preview/testing(set to 1f)
) {
    val total = data.values.sum().coerceAtLeast(1)
    val slices = remember(data) {
        data.values.map { value -> 360f * value / total.toFloat() }
    }

    var started by remember { mutableStateOf(false) }
    val progressAnim by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(durationMillis = animDuration, easing = LinearOutSlowInEasing),
    )
    val progress = progressOverride ?: progressAnim
    val spin by animateFloatAsState(
        targetValue = if (started) 360f else 0f,
        animationSpec = tween(durationMillis = animDuration, easing = LinearOutSlowInEasing),
    )
    LaunchedEffect(Unit) { started = true }

    val gradient1 = Brush.linearGradient(
        colors = listOf(secondaryColor, secondaryColor.copy(alpha = 0.3f)),
        start = Offset(100f, 0f),
        end = Offset(50f, 300f),
    )
    val gradient2 = Brush.linearGradient(
        colors = listOf(primaryColor.copy(alpha = 0.9f), primaryColor.copy(alpha = 0.3f)),
        start = Offset(100f, 20f),
        end = Offset(10f, 300f),
    )
    val gradients = listOf(gradient1, gradient2)

    Box(
        modifier = modifier.size(radiusOuter * 2f),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier = Modifier.size(radiusOuter * 2f),
        ) {
            var last = spin % 360f
            slices.forEachIndexed { index, sweep ->
                drawArc(
                    brush = gradients[index % gradients.size],
                    startAngle = last,
                    sweepAngle = sweep * progress,
                    useCenter = false,
                    style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt),
                )
                last += sweep
            }
        }

        val percentage = (data.values.firstOrNull() ?: 0) * 100 / total
        Text(
            text = "$percentage%",
            color = textColor,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        )
    }
}

@Preview
@Composable
fun PieChartPreview() {
    MonkifyTheme {
        Box(
            modifier = Modifier.size(150.dp)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            PieChart(
                animDuration = 0,
                data = mapOf("A" to 60, "B" to 40),
                progressOverride = 1f,
            )
        }
    }
}
