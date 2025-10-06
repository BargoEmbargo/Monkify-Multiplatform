# Google Ads Integration for iOS

This document explains how to integrate Google Mobile Ads SDK for iOS in this Compose Multiplatform project.

## Overview

The Google Ads integration is implemented using an expect/actual pattern, allowing platform-specific implementations:
- **iOS**: Full Google Mobile Ads SDK integration using Swift
- **Android**: No-op implementation (can be extended if needed)
- **Desktop**: No-op implementation (not applicable)

## Architecture

### Kotlin Multiplatform Layer
- `AdManager` interface (commonMain): Defines the contract for ad operations
- `IOSAdManager` (iosMain): iOS implementation using NSNotificationCenter to communicate with Swift
- `AndroidAdManager` (androidMain): Empty implementation
- `DesktopAdManager` (desktopMain): Empty implementation

### Swift Layer
- `GoogleAdsManager.swift`: Handles actual Google Mobile Ads SDK calls
- Uses `NSNotificationCenter` to communicate bidirectionally with Kotlin code

## Setup Instructions

### 1. Add Google Mobile Ads SDK via Swift Package Manager

1. Open `iosApp.xcodeproj` in Xcode
2. Go to **File > Add Package Dependencies**
3. Enter the repository URL: `https://github.com/googleads/swift-package-manager-google-mobile-ads.git`
4. Select the latest version and click **Add Package**
5. Ensure the package is linked to the `iosApp` target

### 2. Configure Info.plist

Add the following keys to your `iosApp/iosApp/Info.plist`:

```xml
<!-- Replace with your actual AdMob App ID -->
<key>GADApplicationIdentifier</key>
<string>ca-app-pub-XXXXXXXXXXXXXXXX~YYYYYYYYYY</string>

<!-- Optional: Allow arbitrary loads for ad content -->
<key>NSAppTransportSecurity</key>
<dict>
    <key>NSAllowsArbitraryLoads</key>
    <true/>
</dict>

<!-- iOS 14+ App Tracking Transparency -->
<key>NSUserTrackingUsageDescription</key>
<string>This app uses advertising ID to show personalized ads</string>
```

### 3. Get Your AdMob App ID and Ad Unit IDs

1. Create an AdMob account at https://admob.google.com
2. Register your app
3. Create ad units for Banner and/or Interstitial ads
4. Copy your App ID and Ad Unit IDs

### 4. Initialize AdManager

The AdManager is automatically initialized through Koin dependency injection. Initialize it in your app:

```kotlin
// In your MainViewController.kt or App initialization
fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    },
) {
    App()
}

// In your common code, inject AdManager
@Composable
fun YourScreen() {
    val adManager: AdManager = koinInject()
    
    LaunchedEffect(Unit) {
        adManager.initialize()
    }
    
    // Your screen content
}
```

## Usage Examples

### Banner Ads

Use the `BannerAd` composable in your UI:

```kotlin
import cz.uhk.monkify.ads.BannerAd

@Composable
fun YourScreen() {
    Column {
        // Your content
        
        // Banner ad at the bottom
        BannerAd(
            adUnitId = "ca-app-pub-3940256099942544/2934735716", // Test ID
            modifier = Modifier.fillMaxWidth()
        )
    }
}
```

### Interstitial Ads

Load and show interstitial ads programmatically:

```kotlin
import cz.uhk.monkify.ads.AdManager
import org.koin.compose.koinInject

@Composable
fun YourScreen() {
    val adManager: AdManager = koinInject()
    
    LaunchedEffect(Unit) {
        // Initialize ads
        adManager.initialize()
        
        // Load an interstitial ad
        adManager.loadInterstitialAd(
            adUnitId = "ca-app-pub-3940256099942544/4411468910", // Test ID
            onAdLoaded = {
                println("Interstitial ad loaded successfully")
            },
            onAdFailedToLoad = { error ->
                println("Failed to load interstitial ad: $error")
            }
        )
    }
    
    Button(onClick = {
        if (adManager.isInterstitialAdReady()) {
            adManager.showInterstitialAd(
                onAdDismissed = {
                    println("Interstitial ad dismissed")
                    // Reload the ad for next time
                    adManager.loadInterstitialAd(
                        adUnitId = "ca-app-pub-3940256099942544/4411468910"
                    )
                }
            )
        }
    }) {
        Text("Show Interstitial Ad")
    }
}
```

## Test Ad Unit IDs

Use these test ad unit IDs during development:

- **Banner**: `ca-app-pub-3940256099942544/2934735716`
- **Interstitial**: `ca-app-pub-3940256099942544/4411468910`

⚠️ **Important**: Replace test IDs with your actual AdMob ad unit IDs before publishing!

## Communication Flow

1. **Kotlin → Swift**: 
   - Kotlin posts notifications via `NSNotificationCenter`
   - Notifications: `GoogleAdsInitialize`, `GoogleAdsLoadBanner`, `GoogleAdsLoadInterstitial`, `GoogleAdsShowInterstitial`

2. **Swift → Kotlin**:
   - Swift posts notifications back to Kotlin
   - Notifications: `GoogleAdsDidLoad`, `GoogleAdsDidFailToLoad`, `GoogleAdsDidDismiss`

## Troubleshooting

### Ads not showing
- Verify your AdMob App ID is correctly set in Info.plist
- Check Xcode console for Google Ads SDK errors
- Ensure you're using test ad unit IDs during development
- Verify the Google Mobile Ads SDK is properly linked

### Build errors
- Make sure GoogleMobileAds package is added via Swift Package Manager
- Clean build folder (Xcode > Product > Clean Build Folder)
- Restart Xcode

### Runtime errors
- Check that `GoogleAdsManager.swift` is included in the iosApp target
- Verify notification names match between Kotlin and Swift code

## Platform Differences

- **iOS**: Full ads functionality using Google Mobile Ads SDK
- **Android**: No-op implementation (extend `AndroidAdManager` if needed)
- **Desktop**: No-op implementation (not applicable for ads)

## Notes

- This implementation doesn't use CocoaPods or Objective-C as requested
- All iOS-specific code is in Swift
- Communication between Kotlin and Swift uses NSNotificationCenter (no cinterop needed)
- The implementation is ready to extend to Android by modifying `AndroidAdManager.kt`
