package cz.uhk.monkify.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import monkifymultiplatform.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

enum class AchievementEmojis(val resourcePath: String) {
    Fire("files/fire.json"),
    Sad("files/sad.json"),
    Cool("files/cool.json"),
    Wow("files/wow.json"),
}

@Composable
fun DisplayLottieAchievement(emoji: AchievementEmojis, modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes(emoji.resourcePath).decodeToString(),
        )
    }

    Image(
        modifier = modifier.size(66.dp),
        painter = rememberLottiePainter(
            composition = composition,
            iterations = Compottie.IterateForever,
        ),
        contentDescription = emoji.name,
    )
}
