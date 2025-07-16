
package cz.uhk.monkify.wrapper

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cz.uhk.monkify.extension.applyHorizontalScreenPadding
import cz.uhk.monkify.extension.applyHorizontalScreenTabletLimit
import cz.uhk.monkify.extension.drawVerticalScrollbar
import cz.uhk.monkify.extension.whenTrue

object AppDimens {
    val ScreenPaddingDefault: Dp = 32.dp
    val ScreenPaddingMedium: Dp = 24.dp
    val ScreenPaddingHalf: Dp = 16.dp
    val TabletMaxWidth: Dp = 640.dp
    val BottomBarTopPadding: Dp = 16.dp
    val BottomBarBottomPadding: Dp = 32.dp
}

enum class ScreenHorizontalPaddingClass(val value: Dp?) {
    Default(AppDimens.ScreenPaddingDefault),
    Medium(AppDimens.ScreenPaddingMedium),
    Half(AppDimens.ScreenPaddingHalf),
}

@Composable
fun ScreenContentWrapper(
    modifier: Modifier = Modifier,
    isScrollable: Boolean = false,
    limitContentWidthForTablet: Boolean = true,
    showScrollbar: Boolean = true,
    horizontalPaddingClass: ScreenHorizontalPaddingClass = ScreenHorizontalPaddingClass.Default,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    contentVerticalArrangement: Arrangement.Vertical = Arrangement.Top,
    contentAlignment: Alignment = Alignment.TopCenter,
    content: @Composable ColumnScope.() -> Unit,
) {
    val scrollState = rememberScrollState()
        .takeIf { isScrollable }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .whenTrue(isScrollable && showScrollbar) {
                drawVerticalScrollbar(scrollState!!)
            }
            .whenTrue(isScrollable) {
                verticalScroll(scrollState!!)
            },
        contentAlignment = contentAlignment,
    ) {
        Column(
            modifier = Modifier
                .applyHorizontalScreenPadding(horizontalPaddingClass)
                .whenTrue(limitContentWidthForTablet) {
                    applyHorizontalScreenTabletLimit()
                },
            verticalArrangement = contentVerticalArrangement,
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content,
        )
    }
}
