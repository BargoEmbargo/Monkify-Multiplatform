# Google Ads Setup Checklist

Use this checklist to ensure you've completed all steps for Google Ads integration.

## Prerequisites
- [ ] Have an active AdMob account (sign up at https://admob.google.com)
- [ ] Have Xcode installed on your Mac
- [ ] Have the iosApp project ready to configure

## Step 1: AdMob Setup
- [ ] Created an AdMob account
- [ ] Registered your iOS app in AdMob
- [ ] Obtained your AdMob App ID (starts with `ca-app-pub-`)
- [ ] Created a Banner ad unit and copied its ID
- [ ] Created an Interstitial ad unit and copied its ID

## Step 2: Swift Package Manager
- [ ] Opened `iosApp.xcodeproj` in Xcode
- [ ] Added Google Mobile Ads SDK package:
  - URL: `https://github.com/googleads/swift-package-manager-google-mobile-ads.git`
- [ ] Verified package is linked to iosApp target

## Step 3: Info.plist Configuration
- [ ] Added `GADApplicationIdentifier` key with your App ID
- [ ] Added `NSUserTrackingUsageDescription` key with description
- [ ] (Optional) Added `NSAppTransportSecurity` settings
- [ ] (Optional) Added `SKAdNetworkItems` array (see Info.plist.template)

## Step 4: Code Integration
- [ ] Verified `GoogleAdsManager.swift` is in the iosApp target
- [ ] Updated ad unit IDs in your code (or using test IDs)
- [ ] Tested banner ads in a screen
- [ ] Tested interstitial ads

## Step 5: Testing
- [ ] Built the project successfully in Xcode
- [ ] Ran the app on iOS Simulator
- [ ] Verified banner ads load and display
- [ ] Verified interstitial ads load and can be shown
- [ ] Checked Xcode console for any Google Ads errors
- [ ] (Optional) Tested on a physical iOS device

## Step 6: Going Live
- [ ] Replaced all test ad unit IDs with production IDs
- [ ] Tested production ads (they may show "test" initially)
- [ ] Verified app complies with Google AdMob policies
- [ ] Submitted app to App Store (if applicable)

## Troubleshooting Reference
If you encounter issues, check:
- [ ] QUICK_START_GOOGLE_ADS.md - Step-by-step setup guide
- [ ] GOOGLE_ADS_README.md - Full documentation and architecture
- [ ] Xcode console output for specific error messages
- [ ] Google AdMob support: https://support.google.com/admob

## Test Ad Unit IDs (iOS)
For testing purposes:
- **Banner**: `ca-app-pub-3940256099942544/2934735716`
- **Interstitial**: `ca-app-pub-3940256099942544/4411468910`

**Remember**: Replace these with your production ad unit IDs before releasing!

---
**Need help?** Refer to the documentation files in the project root.
