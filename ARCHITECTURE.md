# Google Ads Architecture Diagram

This document provides a visual overview of how the Google Ads integration works in this multiplatform project.

## System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                     Compose Multiplatform App                    │
│                                                                   │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │              Common Code (commonMain)                       │ │
│  │                                                              │ │
│  │  ┌──────────────┐         ┌────────────────┐               │ │
│  │  │  AdManager   │         │   BannerAd     │               │ │
│  │  │  Interface   │◄────────│  Composable    │               │ │
│  │  └──────┬───────┘         └────────────────┘               │ │
│  │         │                                                    │ │
│  │         │ expect/actual                                     │ │
│  │         │                                                    │ │
│  └─────────┼────────────────────────────────────────────────────┘ │
│            │                                                      │
│  ┌─────────▼──────────────────────────────────────────────────┐ │
│  │           Platform-Specific Implementations                 │ │
│  │                                                              │ │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │ │
│  │  │   iosMain    │  │ androidMain  │  │ desktopMain  │     │ │
│  │  │              │  │              │  │              │     │ │
│  │  │ IOSAdManager │  │Android...    │  │Desktop...    │     │ │
│  │  │              │  │ (no-op)      │  │ (no-op)      │     │ │
│  │  └──────┬───────┘  └──────────────┘  └──────────────┘     │ │
│  └─────────┼────────────────────────────────────────────────────┘ │
│            │                                                      │
└────────────┼──────────────────────────────────────────────────────┘
             │
             │ NSNotificationCenter
             │ (Notification-based communication)
             │
┌────────────▼──────────────────────────────────────────────────────┐
│                         iOS Native Layer                           │
│                                                                     │
│  ┌──────────────────────────────────────────────────────────────┐ │
│  │                GoogleAdsManager.swift                         │ │
│  │                                                                │ │
│  │  • Listens to notifications from Kotlin                       │ │
│  │  • Calls Google Mobile Ads SDK                                │ │
│  │  • Posts results back via notifications                       │ │
│  └────────────────────────┬───────────────────────────────────────┘ │
│                           │                                         │
│                           │                                         │
│  ┌────────────────────────▼───────────────────────────────────────┐ │
│  │            Google Mobile Ads SDK (Swift Package)               │ │
│  │                                                                 │ │
│  │  • GADMobileAds.sharedInstance().start()                       │ │
│  │  • GADBannerView                                               │ │
│  │  • GADInterstitialAd                                           │ │
│  └─────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────┘
```

## Communication Flow

### 1. Initialization Flow

```
┌──────────┐         ┌──────────────┐         ┌──────────────────┐
│ Kotlin   │  init   │   iOS Ad     │  notify │  Swift Google    │
│ UI Layer ├────────►│   Manager    ├────────►│  Ads Manager     │
└──────────┘         └──────────────┘         └────────┬─────────┘
                                                        │
                                                        │ start()
                                                        ▼
                                              ┌─────────────────┐
                                              │ Google Ads SDK  │
                                              └─────────────────┘
```

### 2. Banner Ad Loading Flow

```
User Action (Screen Loads)
        │
        ▼
┌─────────────────┐
│  BannerAd()     │ Composable
│  Composable     │
└────────┬────────┘
         │ koinInject()
         ▼
┌─────────────────┐
│  IOSAdManager   │
│  .loadBannerAd()│
└────────┬────────┘
         │ Post notification:
         │ "GoogleAdsLoadBanner"
         ▼
┌─────────────────────┐
│ GoogleAdsManager    │ (Swift)
│ .handleLoadBanner() │
└────────┬────────────┘
         │ GADBannerView.load()
         ▼
┌─────────────────┐
│ Google Ads SDK  │
└────────┬────────┘
         │ Callback: Success/Failure
         ▼
┌─────────────────────┐
│ GoogleAdsManager    │ (Swift)
│ Delegate Methods    │
└────────┬────────────┘
         │ Post notification:
         │ "GoogleAdsDidLoad" or
         │ "GoogleAdsDidFailToLoad"
         ▼
┌─────────────────┐
│  IOSAdManager   │
│  Observer       │
└────────┬────────┘
         │ Invoke callbacks
         ▼
┌─────────────────┐
│  BannerAd()     │
│  UI Updates     │
└─────────────────┘
```

### 3. Interstitial Ad Flow

```
┌─────────────────┐
│ User Action     │
│ (Load Ad)       │
└────────┬────────┘
         │
         ▼
┌───────────────────────┐
│ AdManager             │
│ .loadInterstitialAd() │
└────────┬──────────────┘
         │ Notification: "GoogleAdsLoadInterstitial"
         ▼
┌─────────────────────────┐
│ GoogleAdsManager        │ (Swift)
│ .handleLoadInterstitial()│
└────────┬────────────────┘
         │ GADInterstitialAd.load()
         ▼
┌─────────────────┐
│ Google Ads SDK  │
└────────┬────────┘
         │ Ad Loaded
         ▼
┌────────────────────┐
│ GoogleAdsManager   │ (Swift)
│ Posts success      │
└────────┬───────────┘
         │ Notification: "GoogleAdsDidLoad"
         ▼
┌────────────────────┐
│ IOSAdManager       │
│ interstitialLoaded │
│ = true             │
└────────────────────┘

... Later when user clicks "Show Ad" ...

┌─────────────────┐
│ User Action     │
│ (Show Ad)       │
└────────┬────────┘
         │
         ▼
┌───────────────────────┐
│ AdManager             │
│ .showInterstitialAd() │
└────────┬──────────────┘
         │ Notification: "GoogleAdsShowInterstitial"
         ▼
┌─────────────────────────┐
│ GoogleAdsManager        │ (Swift)
│ .handleShowInterstitial()│
└────────┬────────────────┘
         │ interstitialAd.present()
         ▼
┌─────────────────┐
│ Ad Displays     │
└────────┬────────┘
         │ User dismisses
         ▼
┌────────────────────┐
│ GoogleAdsManager   │ (Swift)
│ Delegate: didDismiss│
└────────┬───────────┘
         │ Notification: "GoogleAdsDidDismiss"
         ▼
┌────────────────────┐
│ IOSAdManager       │
│ Invoke callback    │
└────────────────────┘
```

## Notification Types

### Kotlin → Swift (Commands)
| Notification Name              | Purpose                          | UserInfo Keys      |
|--------------------------------|----------------------------------|--------------------|
| `GoogleAdsInitialize`          | Initialize Google Ads SDK        | None               |
| `GoogleAdsLoadBanner`          | Load a banner ad                 | `adUnitId`         |
| `GoogleAdsLoadInterstitial`    | Load an interstitial ad          | `adUnitId`         |
| `GoogleAdsShowInterstitial`    | Show loaded interstitial ad      | None               |

### Swift → Kotlin (Results)
| Notification Name              | Purpose                          | UserInfo Keys      |
|--------------------------------|----------------------------------|--------------------|
| `GoogleAdsDidLoad`             | Ad loaded successfully           | None               |
| `GoogleAdsDidFailToLoad`       | Ad failed to load                | `error` (String)   |
| `GoogleAdsDidDismiss`          | Interstitial ad was dismissed    | None               |

## Platform Support Matrix

| Platform | AdManager Implementation | Status       |
|----------|-------------------------|--------------|
| iOS      | IOSAdManager            | ✅ Full support with Google Mobile Ads SDK |
| Android  | AndroidAdManager        | ⭕ No-op (can be extended) |
| Desktop  | DesktopAdManager        | ⭕ No-op (not applicable) |

## Key Components

### Kotlin Layer
- **AdManager.kt** (common): Interface definition
- **IOSAdManager.kt**: iOS implementation with NSNotificationCenter
- **BannerAd.kt**: Composable UI component for banner ads
- **AdsExampleScreen.kt**: Example usage screen

### Swift Layer
- **GoogleAdsManager.swift**: Singleton managing Google Ads SDK
  - Observer pattern for notifications
  - Delegate implementations for ad events
  - Root view controller handling

### Dependency Injection
- **KoinModule.ios.kt**: Provides AdManager singleton
- **KoinModule.android.kt**: Provides no-op AdManager
- **KoinModule.desktop.kt**: Provides no-op AdManager

## Why This Architecture?

1. **No CocoaPods**: Uses Swift Package Manager as requested
2. **No Objective-C**: Pure Swift implementation
3. **Clean Separation**: Kotlin and Swift layers are loosely coupled
4. **Type-Safe**: Kotlin multiplatform expect/actual pattern
5. **Composable**: Easy to use in Compose UI
6. **Extensible**: Android implementation can be added easily
7. **Testable**: Platform-specific code is isolated
