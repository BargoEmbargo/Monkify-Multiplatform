package cz.uhk.monkify.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.uhk.monkify.model.AchievementEmojis
import cz.uhk.monkify.model.DisplayLottieAchievement
import cz.uhk.monkify.theme.MonkifyTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeCardTitle(daysCompleted: Int) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            Column {
                Text(
                    text = daysCompleted.toString(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineLarge,
                )
                Text(
                    text = "Streak Days", // todo localization
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            DisplayLottieAchievement(chooseAchievementEmoji(daysCompleted))
        }
        Text(
            text = chooseAchievementText(daysCompleted),
            color = MaterialTheme.colorScheme.onBackground,
        )
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

fun chooseAchievementEmoji(value: Int): AchievementEmojis = when (value) {
    0 -> AchievementEmojis.Sad
    in 1..7 -> AchievementEmojis.Cool
    in 8..15 -> AchievementEmojis.Fire
    in 15..10000 -> AchievementEmojis.Wow
    else -> AchievementEmojis.Sad
}

// todo localization
fun chooseAchievementText(value: Int): String = when (value) {
    0 -> "Don't worry!\nToday is the perfect day to start your streak \uD83D\uDCAA!"
    in 1..7 -> "Keep it up!\nYou're on a roll \uD83D\uDE0E!"
    in 8..15 -> "Impressive!\nYou're building a habit \uD83D\uDC4D."
    in 15..10000 -> "Good job!\nThis is your longest streak so far \uD83D\uDE0D."
    else -> "Don't worry!\nToday is the perfect day to start your streak \uD83D\uDCAA!"
}
