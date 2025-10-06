package cz.uhk.monkify.ads

import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSNotification
import platform.Foundation.NSString
import platform.darwin.NSObject

/**
 * iOS implementation of AdManager.
 * This implementation uses Swift interop to communicate with Google Mobile Ads SDK.
 * 
 * The Swift implementation in iosApp will handle the actual Google Ads SDK calls
 * and communicate back to Kotlin through notifications or callbacks.
 */
class IOSAdManager : AdManager {
    
    private var interstitialAdLoaded = false
    private var onAdLoadedCallback: (() -> Unit)? = null
    private var onAdFailedCallback: ((String) -> Unit)? = null
    private var onAdDismissedCallback: (() -> Unit)? = null
    
    init {
        // Set up notification observers for ad events from Swift
        setupNotificationObservers()
    }
    
    override fun initialize() {
        // Post notification to Swift to initialize Google Ads
        postNotification("GoogleAdsInitialize", null)
    }
    
    override fun loadBannerAd(
        adUnitId: String,
        onAdLoaded: () -> Unit,
        onAdFailedToLoad: (String) -> Unit
    ) {
        onAdLoadedCallback = onAdLoaded
        onAdFailedCallback = onAdFailedToLoad
        
        // Post notification to Swift to load banner ad
        val userInfo = mapOf("adUnitId" to adUnitId, "adType" to "banner")
        postNotification("GoogleAdsLoadBanner", userInfo)
    }
    
    override fun loadInterstitialAd(
        adUnitId: String,
        onAdLoaded: () -> Unit,
        onAdFailedToLoad: (String) -> Unit
    ) {
        onAdLoadedCallback = {
            interstitialAdLoaded = true
            onAdLoaded()
        }
        onAdFailedCallback = { error ->
            interstitialAdLoaded = false
            onAdFailedToLoad(error)
        }
        
        // Post notification to Swift to load interstitial ad
        val userInfo = mapOf("adUnitId" to adUnitId, "adType" to "interstitial")
        postNotification("GoogleAdsLoadInterstitial", userInfo)
    }
    
    override fun showInterstitialAd(onAdDismissed: () -> Unit) {
        if (interstitialAdLoaded) {
            onAdDismissedCallback = {
                interstitialAdLoaded = false
                onAdDismissed()
            }
            // Post notification to Swift to show interstitial ad
            postNotification("GoogleAdsShowInterstitial", null)
        }
    }
    
    override fun isInterstitialAdReady(): Boolean = interstitialAdLoaded
    
    private fun setupNotificationObservers() {
        // Observer for ad loaded successfully
        NSNotificationCenter.defaultCenter.addObserverForName(
            name = "GoogleAdsDidLoad",
            `object` = null,
            queue = null,
            usingBlock = { _: NSNotification? ->
                onAdLoadedCallback?.invoke()
            }
        )
        
        // Observer for ad failed to load
        NSNotificationCenter.defaultCenter.addObserverForName(
            name = "GoogleAdsDidFailToLoad",
            `object` = null,
            queue = null,
            usingBlock = { notification: NSNotification? ->
                val error = notification?.userInfo?.get("error") as? NSString
                onAdFailedCallback?.invoke(error?.toString() ?: "Unknown error")
            }
        )
        
        // Observer for ad dismissed
        NSNotificationCenter.defaultCenter.addObserverForName(
            name = "GoogleAdsDidDismiss",
            `object` = null,
            queue = null,
            usingBlock = { _: NSNotification? ->
                onAdDismissedCallback?.invoke()
            }
        )
    }
    
    private fun postNotification(name: String, userInfo: Map<String, Any>?) {
        NSNotificationCenter.defaultCenter.postNotificationName(
            aName = name,
            `object` = null,
            userInfo = userInfo as? Map<Any?, *>
        )
    }
}

actual fun createAdManager(): AdManager = IOSAdManager()
