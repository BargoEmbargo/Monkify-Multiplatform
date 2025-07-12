import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import monkifymultiplatform.composeapp.generated.resources.Res

@Composable
fun LottieImage(
    filePath: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    iterations: Int = Compottie.IterateForever,
) {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes(filePath).decodeToString(),
        )
    }

    Image(
        painter = rememberLottiePainter(
            composition = composition,
            iterations = iterations,
        ),
        contentDescription = contentDescription,
        modifier = modifier,
    )
}
