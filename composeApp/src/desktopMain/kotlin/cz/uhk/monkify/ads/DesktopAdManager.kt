package cz.uhk.monkify.ads

/**
 * Desktop implementation of AdManager.
 * Currently a no-op implementation as ads are only needed for iOS.
 */
class DesktopAdManager : AdManager {
    
    override fun initialize() {
        // No-op for Desktop
    }
    
    override fun loadBannerAd(
        adUnitId: String,
        onAdLoaded: () -> Unit,
        onAdFailedToLoad: (String) -> Unit
    ) {
        // No-op for Desktop
    }
    
    override fun loadInterstitialAd(
        adUnitId: String,
        onAdLoaded: () -> Unit,
        onAdFailedToLoad: (String) -> Unit
    ) {
        // No-op for Desktop
    }
    
    override fun showInterstitialAd(onAdDismissed: () -> Unit) {
        // No-op for Desktop
    }
    
    override fun isInterstitialAdReady(): Boolean = false
}

actual fun createAdManager(): AdManager = DesktopAdManager()
