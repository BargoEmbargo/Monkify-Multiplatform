package cz.uhk.monkify.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cz.uhk.monkify.model.DisplayLottieAchievement
import cz.uhk.monkify.theme.MonkifyTheme
import cz.uhk.monkify.util.chooseAchievementEmoji
import cz.uhk.monkify.util.chooseAchievementText
import monkifymultiplatform.composeapp.generated.resources.Res
import monkifymultiplatform.composeapp.generated.resources.streak_days
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeCardTitle(daysCompleted: Int) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            Column {
                Text(
                    text = daysCompleted.toString(),
                    style = MaterialTheme.typography.headlineLarge,
                )
                Text(
                    text = stringResource(Res.string.streak_days),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            DisplayLottieAchievement(chooseAchievementEmoji(daysCompleted))
        }
        Text(text = chooseAchievementText(daysCompleted))
    }
}

@Preview
@Composable
private fun HomeCardTitlePreview() {
    MonkifyTheme {
        GlassmorpismCard {
            HomeCardTitle(5)
        }
    }
}
