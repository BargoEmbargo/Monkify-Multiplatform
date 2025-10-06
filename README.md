This is a Kotlin Multiplatform project targeting Android, iOS, Desktop.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…

## Google Ads Integration (iOS)

This project includes Google Ads integration for iOS using Swift Package Manager (no CocoaPods or Objective-C).

📖 **Quick Start**: See [QUICK_START_GOOGLE_ADS.md](QUICK_START_GOOGLE_ADS.md) for step-by-step setup instructions.

📚 **Full Documentation**: See [GOOGLE_ADS_README.md](GOOGLE_ADS_README.md) for architecture details and advanced usage.

### Key Features:
- ✅ Banner and Interstitial ads support
- ✅ Platform-specific implementation using expect/actual pattern
- ✅ Swift integration (no CocoaPods, no Objective-C)
- ✅ Koin dependency injection ready
- ✅ Composable UI components included
