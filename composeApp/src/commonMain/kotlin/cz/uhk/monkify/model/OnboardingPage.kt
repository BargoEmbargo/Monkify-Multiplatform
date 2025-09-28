package cz.uhk.monkify.model

import androidx.compose.runtime.Composable
import monkifymultiplatform.composeapp.generated.resources.Res
import monkifymultiplatform.composeapp.generated.resources.onboarding_desc_1
import monkifymultiplatform.composeapp.generated.resources.onboarding_desc_2
import monkifymultiplatform.composeapp.generated.resources.onboarding_desc_3
import monkifymultiplatform.composeapp.generated.resources.onboarding_title_1
import monkifymultiplatform.composeapp.generated.resources.onboarding_title_2
import monkifymultiplatform.composeapp.generated.resources.onboarding_title_3
import monkifymultiplatform.composeapp.generated.resources.page_one
import monkifymultiplatform.composeapp.generated.resources.page_three
import monkifymultiplatform.composeapp.generated.resources.page_two
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource

data class OnboardingPage(
    val title: String,
    val description: String,
    val imageRes: DrawableResource,
)

@Composable
fun getOnboardingPages(): List<OnboardingPage> = listOf(
    OnboardingPage(
        title = stringResource(Res.string.onboarding_title_1),
        description = stringResource(Res.string.onboarding_desc_1),
        imageRes = Res.drawable.page_one,
    ),
    OnboardingPage(
        title = stringResource(Res.string.onboarding_title_2),
        description = stringResource(Res.string.onboarding_desc_2),
        imageRes = Res.drawable.page_two,
    ),
    OnboardingPage(
        title = stringResource(Res.string.onboarding_title_3),
        description = stringResource(Res.string.onboarding_desc_3),
        imageRes = Res.drawable.page_three,
    ),
)
