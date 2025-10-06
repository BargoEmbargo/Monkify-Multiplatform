package cz.uhk.monkify.ads

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject

/**
 * Example screen showing how to use Google Ads in your Compose UI.
 * 
 * This is just a reference implementation. You can integrate ads
 * into any of your existing screens following this pattern.
 */
@Composable
fun AdsExampleScreen() {
    val adManager: AdManager = koinInject()
    
    // Load interstitial ad when screen appears
    LaunchedEffect(Unit) {
        adManager.loadInterstitialAd(
            adUnitId = "ca-app-pub-3940256099942544/4411468910", // Test ad unit ID
            onAdLoaded = {
                println("Interstitial ad loaded successfully")
            },
            onAdFailedToLoad = { error ->
                println("Failed to load interstitial ad: $error")
            }
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Google Ads Example",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Text(
                text = "This screen demonstrates how to integrate Google Ads in your Compose Multiplatform app.",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {
                    if (adManager.isInterstitialAdReady()) {
                        adManager.showInterstitialAd(
                            onAdDismissed = {
                                println("Interstitial ad dismissed")
                                // Reload ad for next time
                                adManager.loadInterstitialAd(
                                    adUnitId = "ca-app-pub-3940256099942544/4411468910"
                                )
                            }
                        )
                    }
                }
            ) {
                Text("Show Interstitial Ad")
            }
            
            Text(
                text = if (adManager.isInterstitialAdReady()) {
                    "Interstitial ad is ready!"
                } else {
                    "Loading interstitial ad..."
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // Banner ad at bottom
        Column {
            Text(
                text = "Banner Ad:",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            BannerAd(
                adUnitId = "ca-app-pub-3940256099942544/2934735716", // Test ad unit ID
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
