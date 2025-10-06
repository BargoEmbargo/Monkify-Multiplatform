package cz.uhk.monkify.ads

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject

/**
 * Composable that displays a banner ad.
 * 
 * @param adUnitId The ad unit ID for the banner ad
 * @param modifier Modifier for the ad container
 */
@Composable
fun BannerAd(
    adUnitId: String,
    modifier: Modifier = Modifier
) {
    val adManager: AdManager = koinInject()
    var isAdLoaded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    LaunchedEffect(adUnitId) {
        adManager.loadBannerAd(
            adUnitId = adUnitId,
            onAdLoaded = {
                isAdLoaded = true
                errorMessage = null
            },
            onAdFailedToLoad = { error ->
                isAdLoaded = false
                errorMessage = error
            }
        )
    }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            errorMessage != null -> {
                // Optional: Show error state
                // Text(text = "Ad failed to load: $errorMessage")
            }
            !isAdLoaded -> {
                // Optional: Show loading state
                // CircularProgressIndicator()
            }
            else -> {
                // Ad is loaded - actual banner will be shown by native code
                // This is just a placeholder in the UI
            }
        }
    }
}
