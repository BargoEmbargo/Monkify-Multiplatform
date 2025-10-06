package cz.uhk.monkify.ads

/**
 * Android implementation of AdManager.
 * Currently a no-op implementation as ads are only needed for iOS.
 */
class AndroidAdManager : AdManager {
    
    override fun initialize() {
        // No-op for Android
    }
    
    override fun loadBannerAd(
        adUnitId: String,
        onAdLoaded: () -> Unit,
        onAdFailedToLoad: (String) -> Unit
    ) {
        // No-op for Android
    }
    
    override fun loadInterstitialAd(
        adUnitId: String,
        onAdLoaded: () -> Unit,
        onAdFailedToLoad: (String) -> Unit
    ) {
        // No-op for Android
    }
    
    override fun showInterstitialAd(onAdDismissed: () -> Unit) {
        // No-op for Android
    }
    
    override fun isInterstitialAdReady(): Boolean = false
}

actual fun createAdManager(): AdManager = AndroidAdManager()
