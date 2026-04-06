package cz.uhk.monkify.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.uhk.monkify.model.OnboardingPage
import cz.uhk.monkify.model.getOnboardingPages
import cz.uhk.monkify.theme.MonkifyTheme
import cz.uhk.monkify.wrapper.ScreenContentWrapper
import kotlinx.coroutines.launch
import monkifymultiplatform.composeapp.generated.resources.Res
import monkifymultiplatform.composeapp.generated.resources.onboarding_back
import monkifymultiplatform.composeapp.generated.resources.onboarding_desc_1
import monkifymultiplatform.composeapp.generated.resources.onboarding_desc_2
import monkifymultiplatform.composeapp.generated.resources.onboarding_desc_3
import monkifymultiplatform.composeapp.generated.resources.onboarding_finish
import monkifymultiplatform.composeapp.generated.resources.onboarding_next
import monkifymultiplatform.composeapp.generated.resources.onboarding_title_1
import monkifymultiplatform.composeapp.generated.resources.onboarding_title_2
import monkifymultiplatform.composeapp.generated.resources.onboarding_title_3
import monkifymultiplatform.composeapp.generated.resources.page_one
import monkifymultiplatform.composeapp.generated.resources.page_three
import monkifymultiplatform.composeapp.generated.resources.page_two
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pages = getOnboardingPages()
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    OnboardingScreenContent(
        pages = pages,
        pagerState = pagerState,
        onBack = { coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } },
        onNext = { coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
        onFinish = onFinish,
    )
}

@Composable
private fun OnboardingScreenContent(
    pages: List<OnboardingPage>,
    pagerState: PagerState,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onFinish: () -> Unit,
) {
    ScreenContentWrapper(showScrollbar = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                OnboardingPageContent(page = pages[page])
            }
            OnboardingNavigationRow(
                pagesCount = pages.size,
                currentPage = pagerState.currentPage,
                onBack = onBack,
                onNext = onNext,
                onFinish = onFinish,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun OnboardingButton(
    enabled: Boolean,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (enabled) {
        TextButton(onClick = onClick, modifier = modifier.fillMaxWidth()) {
            Text(text)
        }
    } else {
        Spacer(modifier = modifier)
    }
}

@Composable
private fun OnboardingDots(
    pagesCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pagesCount) { index ->
            val color = if (currentPage == index) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            }
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(10.dp)
                    .background(color, shape = MaterialTheme.shapes.small),
            )
        }
    }
}

@Composable
private fun OnboardingNavigationRow(
    pagesCount: Int,
    currentPage: Int,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val buttonWidth = 96.dp
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OnboardingButton(
            enabled = currentPage > 0,
            text = stringResource(Res.string.onboarding_back),
            onClick = onBack,
            modifier = Modifier.width(buttonWidth),
        )
        OnboardingDots(
            pagesCount = pagesCount,
            currentPage = currentPage,
            modifier = Modifier.weight(1f),
        )
        if (currentPage < pagesCount - 1) {
            OnboardingButton(
                enabled = true,
                text = stringResource(Res.string.onboarding_next),
                onClick = onNext,
                modifier = Modifier.width(buttonWidth),
            )
        } else {
            OnboardingButton(
                enabled = true,
                text = stringResource(Res.string.onboarding_finish),
                onClick = onFinish,
                modifier = Modifier.width(buttonWidth),
            )
        }
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(page.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .padding(bottom = 16.dp),
            )
            Text(
                text = page.title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp),
            )
            Text(
                text = page.description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@Preview
@Composable
private fun OnboardingScreenContentPreview() {
    val pages = getOnboardingPages()
    val pagerState = PagerState(pageCount = { pages.size })

    MonkifyTheme {
        OnboardingScreenContent(
            pages = pages,
            pagerState = pagerState,
            onBack = {},
            onNext = {},
            onFinish = {},
        )
    }
}
