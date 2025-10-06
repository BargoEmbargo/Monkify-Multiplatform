# Quick Start Guide: Google Ads Integration

Follow these steps to get Google Ads working in your iOS app:

## Step 1: Add Google Mobile Ads SDK

1. Open `iosApp.xcodeproj` in Xcode
2. Select your project in the navigator
3. Go to **File → Add Package Dependencies...**
4. Paste this URL: `https://github.com/googleads/swift-package-manager-google-mobile-ads.git`
5. Click **Add Package**
6. Select `GoogleMobileAds` and click **Add Package** again

## Step 2: Get Your AdMob Credentials

1. Go to https://admob.google.com/
2. Sign in or create an account
3. Click **Apps → Add App**
4. Follow the wizard to register your iOS app
5. After creation, you'll get:
   - **App ID**: Something like `ca-app-pub-1234567890123456~1234567890`
   - **Ad Unit IDs**: You'll create these for Banner and Interstitial ads

### Creating Ad Units

1. In AdMob console, select your app
2. Click **Ad units → Get started** (or **Add ad unit**)
3. Choose **Banner** or **Interstitial**
4. Fill in the details and click **Create ad unit**
5. Copy the **Ad unit ID** (e.g., `ca-app-pub-1234567890123456/1234567890`)
6. Repeat for each ad type you want to use

## Step 3: Configure Info.plist

Open `iosApp/iosApp/Info.plist` in Xcode and add these entries:

**Right-click in the plist → Add Row**, then add:

### Required: AdMob App ID
- **Key**: `GADApplicationIdentifier` (or paste: `Google AdMob App ID`)
- **Type**: String
- **Value**: Your App ID from Step 2 (e.g., `ca-app-pub-1234567890123456~1234567890`)

### Required: User Tracking Description (iOS 14+)
- **Key**: `NSUserTrackingUsageDescription` (or paste: `Privacy - Tracking Usage Description`)
- **Type**: String  
- **Value**: `This app uses your advertising identifier to show you personalized ads`

### Optional: Allow HTTP loads for ads
- **Key**: `NSAppTransportSecurity` (or paste: `App Transport Security Settings`)
- **Type**: Dictionary
  - Add child: **Key**: `NSAllowsArbitraryLoads`, **Type**: Boolean, **Value**: `YES`

> See `Info.plist.template` in the `iosApp` folder for a complete example including SKAdNetwork identifiers.

## Step 4: Use Ads in Your Code

### Initialize AdManager (Already Done!)

The `MainViewController.kt` already initializes ads on startup. No action needed.

### Show a Banner Ad

Add this to any screen:

```kotlin
import cz.uhk.monkify.ads.BannerAd

@Composable
fun MyScreen() {
    Column {
        // Your content here
        
        // Banner ad at bottom
        BannerAd(
            adUnitId = "YOUR_BANNER_AD_UNIT_ID",
            modifier = Modifier.fillMaxWidth()
        )
    }
}
```

### Show an Interstitial Ad

```kotlin
import cz.uhk.monkify.ads.AdManager
import org.koin.compose.koinInject

@Composable
fun MyScreen() {
    val adManager: AdManager = koinInject()
    
    // Load ad when screen appears
    LaunchedEffect(Unit) {
        adManager.loadInterstitialAd(
            adUnitId = "YOUR_INTERSTITIAL_AD_UNIT_ID",
            onAdLoaded = { println("Ad loaded!") },
            onAdFailedToLoad = { error -> println("Error: $error") }
        )
    }
    
    // Show ad on button click
    Button(onClick = {
        if (adManager.isInterstitialAdReady()) {
            adManager.showInterstitialAd(
                onAdDismissed = {
                    // Optionally reload the ad
                    adManager.loadInterstitialAd(
                        adUnitId = "YOUR_INTERSTITIAL_AD_UNIT_ID"
                    )
                }
            )
        }
    }) {
        Text("Show Ad")
    }
}
```

## Step 5: Test with Sample Ads

During development, use Google's **test ad unit IDs**:

- **Banner (iOS)**: `ca-app-pub-3940256099942544/2934735716`
- **Interstitial (iOS)**: `ca-app-pub-3940256099942544/4411468910`

Replace these with your real ad unit IDs before publishing!

## Step 6: Build and Run

1. Clean your build: **Product → Clean Build Folder** (⇧⌘K)
2. Build: **Product → Build** (⌘B)
3. Run on simulator or device: **Product → Run** (⌘R)

## Troubleshooting

### "GADApplicationIdentifier not found"
- Check that you added the key to Info.plist correctly
- Key should be exactly: `GADApplicationIdentifier`
- Value should start with `ca-app-pub-`

### Ads not showing
- Are you using test ad unit IDs? (See Step 5)
- Check Xcode console for error messages
- Make sure the Google Mobile Ads SDK was added successfully
- Try on a real device instead of simulator

### Build errors
- Clean build folder and rebuild
- Check that `GoogleAdsManager.swift` is in the iosApp target
- Verify the package was added correctly in Xcode

## Example Implementation

See `AdsExampleScreen.kt` in `composeApp/src/commonMain/kotlin/cz/uhk/monkify/ads/` for a complete working example.

## Need More Help?

Check the full documentation in `GOOGLE_ADS_README.md` for:
- Architecture details
- Advanced usage
- Platform differences
- Communication flow between Kotlin and Swift
