package cz.uhk.monkify.util

import androidx.compose.runtime.Composable
import cz.uhk.monkify.model.AchievementEmojis
import monkifymultiplatform.composeapp.generated.resources.Res
import monkifymultiplatform.composeapp.generated.resources.achievement_text_0
import monkifymultiplatform.composeapp.generated.resources.achievement_text_16_plus
import monkifymultiplatform.composeapp.generated.resources.achievement_text_1_7
import monkifymultiplatform.composeapp.generated.resources.achievement_text_8_15
import org.jetbrains.compose.resources.stringResource

fun chooseAchievementEmoji(value: Int): AchievementEmojis = when (value) {
    0 -> AchievementEmojis.Sad
    in 1..7 -> AchievementEmojis.Cool
    in 8..15 -> AchievementEmojis.Fire
    in 15..10000 -> AchievementEmojis.Wow
    else -> AchievementEmojis.Sad
}

@Composable
fun chooseAchievementText(value: Int): String = when (value) {
    0 -> stringResource(Res.string.achievement_text_0)
    in 1..7 -> stringResource(Res.string.achievement_text_1_7)
    in 8..15 -> stringResource(Res.string.achievement_text_8_15)
    in 16..10000 -> stringResource(Res.string.achievement_text_16_plus)
    else -> stringResource(Res.string.achievement_text_0)
}
