
package cz.uhk.monkify.extension

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastSumBy
import cz.uhk.monkify.wrapper.AppDimens
import cz.uhk.monkify.wrapper.ScreenHorizontalPaddingClass
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest

/*
 * MIT License
 *
 * Copyright (c) 2022 Albert Chang
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
 * Extension modifiers for scrollbars and layout wrappers.
 * Adapted from Albert Chang’s scrollbar gist:
 * https://gist.github.com/mxalbert1996/33a360fcab2105a31e5355af98216f5a
 * and modified for Monkify Multiplatform project.
 */

fun Modifier.conditional(
    condition: Boolean,
    yes: Modifier.() -> Modifier,
    no: Modifier.() -> Modifier = { this },
): Modifier = if (condition) yes(this) else no(this)

fun Modifier.whenTrue(condition: Boolean, apply: Modifier.() -> Modifier): Modifier = if (condition) apply(this) else this

fun Modifier.applyHorizontalScreenPadding(paddingClass: ScreenHorizontalPaddingClass = ScreenHorizontalPaddingClass.Medium): Modifier =
    paddingClass.value?.let { padding(horizontal = it) } ?: this

fun Modifier.applyVerticalScreenPadding(paddingClass: ScreenHorizontalPaddingClass = ScreenHorizontalPaddingClass.Medium): Modifier =
    paddingClass.value?.let { padding(vertical = it) } ?: this

fun Modifier.applyHorizontalScreenTabletLimit(): Modifier = widthIn(max = AppDimens.TabletMaxWidth)

fun Modifier.applyHorizontalScreenPaddingAndTabletLimit(
    paddingClass: ScreenHorizontalPaddingClass = ScreenHorizontalPaddingClass.Medium,
): Modifier = applyHorizontalScreenPadding(paddingClass)
    .applyHorizontalScreenTabletLimit()

fun Modifier.applyBottomBarVerticalPadding(): Modifier = padding(
    top = AppDimens.BottomBarTopPadding,
    bottom = AppDimens.BottomBarBottomPadding,
)

private val ScrollBarThickness = 4.dp
private const val ScrollBarFadeDelayMillis = 150
private const val ScrollBarFadeDurationMillis = 250
private val FadeOutAnimationSpec = tween<Float>(durationMillis = ScrollBarFadeDurationMillis)

private val BarColor: Color
    @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)

fun Modifier.drawVerticalScrollbar(
    state: ScrollState,
    reverseScrolling: Boolean = false,
    fadeOut: Boolean = true,
): Modifier = drawScrollbar(state, Orientation.Vertical, reverseScrolling, fadeOut)

fun Modifier.drawHorizontalScrollbar(
    state: ScrollState,
    reverseScrolling: Boolean = false,
    fadeOut: Boolean = true,
): Modifier = drawScrollbar(state, Orientation.Horizontal, reverseScrolling, fadeOut)

private fun Modifier.drawScrollbar(
    state: ScrollState,
    orientation: Orientation,
    reverseScrolling: Boolean,
    fadeOut: Boolean,
): Modifier = drawScrollbarBase(orientation, reverseScrolling, fadeOut) { reverseDir, atEnd, color, alpha ->
    if (state.maxValue > 0) {
        val canvasSize = if (orientation == Orientation.Horizontal) size.width else size.height
        val totalSize = canvasSize + state.maxValue
        val thumbSize = canvasSize / totalSize * canvasSize
        val startOff = state.value / totalSize * canvasSize
        drawScrollbarThumb(orientation, reverseDir, atEnd, color, alpha, thumbSize, startOff)
    }
}

fun Modifier.drawVerticalScrollbar(
    state: LazyListState,
    reverseScrolling: Boolean = false,
    fadeOut: Boolean = true,
): Modifier = drawScrollbar(state, Orientation.Vertical, reverseScrolling, fadeOut)

fun Modifier.drawHorizontalScrollbar(
    state: LazyListState,
    reverseScrolling: Boolean = false,
    fadeOut: Boolean = true,
): Modifier = drawScrollbar(state, Orientation.Horizontal, reverseScrolling, fadeOut)

private fun Modifier.drawScrollbar(
    state: LazyListState,
    orientation: Orientation,
    reverseScrolling: Boolean,
    fadeOut: Boolean,
): Modifier = drawScrollbarBase(orientation, reverseScrolling, fadeOut) { reverseDir, atEnd, color, alpha ->
    val info = state.layoutInfo
    val viewportSize = info.viewportEndOffset - info.viewportStartOffset
    val visibleItems = info.visibleItemsInfo
    val itemsSize = visibleItems.fastSumBy { it.size }
    if (visibleItems.size < info.totalItemsCount || itemsSize > viewportSize) {
        val estItemSize = if (visibleItems.isEmpty()) 0f else itemsSize.toFloat() / visibleItems.size
        val totalSize = estItemSize * info.totalItemsCount
        val canvasSize = if (orientation == Orientation.Horizontal) size.width else size.height
        val thumbSize = viewportSize / totalSize * canvasSize
        val startOff = if (visibleItems.isEmpty()) {
            0f
        } else {
            visibleItems.first().run {
                (estItemSize * index - offset) / totalSize * canvasSize
            }
        }
        drawScrollbarThumb(orientation, reverseDir, atEnd, color, alpha, thumbSize, startOff)
    }
}

fun Modifier.drawVerticalScrollbar(
    state: LazyGridState,
    spanCount: Int,
    reverseScrolling: Boolean = false,
    fadeOut: Boolean = true,
): Modifier = drawScrollbarBase(Orientation.Vertical, reverseScrolling, fadeOut) { reverseDir, atEnd, color, alpha ->
    val info = state.layoutInfo
    val viewportSize = info.viewportEndOffset - info.viewportStartOffset
    val visibleItems = info.visibleItemsInfo
    val rowCount = (visibleItems.size + spanCount - 1) / spanCount
    val itemsSize = (0 until rowCount).sumOf { visibleItems[it * spanCount].size.height }
    if (visibleItems.size < info.totalItemsCount || itemsSize > viewportSize) {
        val estItemSize = if (rowCount == 0) 0f else itemsSize.toFloat() / rowCount
        val totalRows = (info.totalItemsCount + spanCount - 1) / spanCount
        val totalSize = estItemSize * totalRows
        val canvasSize = size.height
        val thumbSize = viewportSize / totalSize * canvasSize
        val startOff = if (rowCount == 0) {
            0f
        } else {
            visibleItems.first().run {
                val rowIdx = index / spanCount
                (estItemSize * rowIdx - offset.y) / totalSize * canvasSize
            }
        }
        drawScrollbarThumb(Orientation.Vertical, reverseDir, atEnd, color, alpha, thumbSize, startOff)
    }
}

private fun Modifier.drawScrollbarBase(
    orientation: Orientation,
    reverseScrolling: Boolean,
    fadeOut: Boolean,
    onDraw: DrawScope.(reverseDir: Boolean, atEnd: Boolean, color: Color, alpha: () -> Float) -> Unit,
): Modifier = composed {
    val scrolled = remember {
        MutableSharedFlow<Unit>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    }
    val conn = remember(orientation) {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                val delta = if (orientation == Orientation.Horizontal) consumed.x else consumed.y
                if (delta != 0f) scrolled.tryEmit(Unit)
                return Offset.Zero
            }
        }
    }
    val alpha = remember { Animatable(0f) }
    LaunchedEffect(fadeOut) {
        if (fadeOut) {
            scrolled.collectLatest {
                alpha.snapTo(1f)
                delay(ScrollBarFadeDelayMillis.toLong())
                alpha.animateTo(0f, animationSpec = FadeOutAnimationSpec)
            }
        } else {
            alpha.snapTo(1f)
        }
    }

    val isLtr = LocalLayoutDirection.current == LayoutDirection.Ltr
    val reverseDir = if (orientation == Orientation.Horizontal) {
        if (isLtr) reverseScrolling else !reverseScrolling
    } else {
        reverseScrolling
    }
    val atEnd = if (orientation == Orientation.Vertical) isLtr else true
    val color = BarColor

    Modifier
        .nestedScroll(conn)
        .drawWithContent {
            drawContent()
            onDraw(reverseDir, atEnd, color, alpha::value)
        }
}

private fun DrawScope.drawScrollbarThumb(
    orientation: Orientation,
    reverseDir: Boolean,
    atEnd: Boolean,
    color: Color,
    alpha: () -> Float,
    thumbSize: Float,
    startOff: Float,
) {
    val thicknessPx = ScrollBarThickness.toPx()
    val topLeft = if (orientation == Orientation.Horizontal) {
        Offset(
            if (reverseDir) size.width - startOff - thumbSize else startOff,
            if (atEnd) size.height - thicknessPx else 0f,
        )
    } else {
        Offset(
            if (atEnd) size.width - thicknessPx else 0f,
            if (reverseDir) size.height - startOff - thumbSize else startOff,
        )
    }
    val barSize = if (orientation == Orientation.Horizontal) {
        Size(thumbSize, thicknessPx)
    } else {
        Size(thicknessPx, thumbSize)
    }
    drawRect(color = color, topLeft = topLeft, size = barSize, alpha = alpha())
}
