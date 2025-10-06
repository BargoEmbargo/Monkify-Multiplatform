package cz.uhk.monkify.ads

/**
 * Common interface for ad management across platforms.
 * This interface defines methods for loading and displaying ads.
 */
interface AdManager {
    /**
     * Initialize the ad manager with the necessary configuration.
     */
    fun initialize()
    
    /**
     * Load a banner ad.
     * @param adUnitId The ad unit ID for the banner ad
     * @param onAdLoaded Callback when ad is successfully loaded
     * @param onAdFailedToLoad Callback when ad fails to load with error message
     */
    fun loadBannerAd(
        adUnitId: String,
        onAdLoaded: () -> Unit = {},
        onAdFailedToLoad: (String) -> Unit = {}
    )
    
    /**
     * Load an interstitial ad.
     * @param adUnitId The ad unit ID for the interstitial ad
     * @param onAdLoaded Callback when ad is successfully loaded
     * @param onAdFailedToLoad Callback when ad fails to load with error message
     */
    fun loadInterstitialAd(
        adUnitId: String,
        onAdLoaded: () -> Unit = {},
        onAdFailedToLoad: (String) -> Unit = {}
    )
    
    /**
     * Show an interstitial ad if one is loaded.
     * @param onAdDismissed Callback when ad is dismissed
     */
    fun showInterstitialAd(onAdDismissed: () -> Unit = {})
    
    /**
     * Check if an interstitial ad is ready to be shown.
     * @return true if an interstitial ad is loaded and ready
     */
    fun isInterstitialAdReady(): Boolean
}

/**
 * Platform-specific factory function to create AdManager instance.
 */
expect fun createAdManager(): AdManager
