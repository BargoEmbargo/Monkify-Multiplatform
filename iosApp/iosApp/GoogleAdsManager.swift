import Foundation
import UIKit
import GoogleMobileAds

/**
 * GoogleAdsManager handles the integration with Google Mobile Ads SDK.
 * It communicates with Kotlin code through NSNotificationCenter.
 * 
 * IMPORTANT: Before using this code:
 * 1. Add Google Mobile Ads SDK via Swift Package Manager:
 *    - In Xcode, go to File > Add Package Dependencies
 *    - Add: https://github.com/googleads/swift-package-manager-google-mobile-ads.git
 * 2. Add your AdMob App ID to Info.plist:
 *    <key>GADApplicationIdentifier</key>
 *    <string>ca-app-pub-XXXXXXXXXXXXXXXX~YYYYYYYYYY</string>
 * 3. Add App Transport Security settings to Info.plist if needed:
 *    <key>NSAppTransportSecurity</key>
 *    <dict>
 *        <key>NSAllowsArbitraryLoads</key>
 *        <true/>
 *    </dict>
 */
class GoogleAdsManager: NSObject {
    static let shared = GoogleAdsManager()
    
    private var interstitialAd: GADInterstitialAd?
    private var bannerView: GADBannerView?
    
    private override init() {
        super.init()
        setupNotificationObservers()
    }
    
    // MARK: - Notification Observers
    
    private func setupNotificationObservers() {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleInitialize),
            name: NSNotification.Name("GoogleAdsInitialize"),
            object: nil
        )
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleLoadBanner(_:)),
            name: NSNotification.Name("GoogleAdsLoadBanner"),
            object: nil
        )
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleLoadInterstitial(_:)),
            name: NSNotification.Name("GoogleAdsLoadInterstitial"),
            object: nil
        )
        
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleShowInterstitial),
            name: NSNotification.Name("GoogleAdsShowInterstitial"),
            object: nil
        )
    }
    
    // MARK: - Initialize
    
    @objc private func handleInitialize() {
        DispatchQueue.main.async {
            GADMobileAds.sharedInstance().start { status in
                print("Google Mobile Ads SDK initialized")
            }
        }
    }
    
    // MARK: - Banner Ads
    
    @objc private func handleLoadBanner(_ notification: Notification) {
        guard let userInfo = notification.userInfo,
              let adUnitId = userInfo["adUnitId"] as? String else {
            notifyAdFailedToLoad(error: "Missing adUnitId")
            return
        }
        
        DispatchQueue.main.async { [weak self] in
            guard let self = self,
                  let rootViewController = self.getRootViewController() else {
                self?.notifyAdFailedToLoad(error: "No root view controller")
                return
            }
            
            self.bannerView = GADBannerView(adSize: GADAdSizeBanner)
            self.bannerView?.adUnitID = adUnitId
            self.bannerView?.rootViewController = rootViewController
            self.bannerView?.delegate = self
            
            let request = GADRequest()
            self.bannerView?.load(request)
        }
    }
    
    // MARK: - Interstitial Ads
    
    @objc private func handleLoadInterstitial(_ notification: Notification) {
        guard let userInfo = notification.userInfo,
              let adUnitId = userInfo["adUnitId"] as? String else {
            notifyAdFailedToLoad(error: "Missing adUnitId")
            return
        }
        
        DispatchQueue.main.async { [weak self] in
            let request = GADRequest()
            GADInterstitialAd.load(
                withAdUnitID: adUnitId,
                request: request
            ) { [weak self] ad, error in
                if let error = error {
                    self?.notifyAdFailedToLoad(error: error.localizedDescription)
                    return
                }
                
                self?.interstitialAd = ad
                self?.interstitialAd?.fullScreenContentDelegate = self
                self?.notifyAdLoaded()
            }
        }
    }
    
    @objc private func handleShowInterstitial() {
        DispatchQueue.main.async { [weak self] in
            guard let self = self,
                  let interstitialAd = self.interstitialAd,
                  let rootViewController = self.getRootViewController() else {
                return
            }
            
            interstitialAd.present(fromRootViewController: rootViewController)
        }
    }
    
    // MARK: - Helper Methods
    
    private func getRootViewController() -> UIViewController? {
        guard let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
              let window = windowScene.windows.first,
              let rootViewController = window.rootViewController else {
            return nil
        }
        return rootViewController
    }
    
    private func notifyAdLoaded() {
        NotificationCenter.default.post(
            name: NSNotification.Name("GoogleAdsDidLoad"),
            object: nil
        )
    }
    
    private func notifyAdFailedToLoad(error: String) {
        NotificationCenter.default.post(
            name: NSNotification.Name("GoogleAdsDidFailToLoad"),
            object: nil,
            userInfo: ["error": error]
        )
    }
    
    private func notifyAdDismissed() {
        NotificationCenter.default.post(
            name: NSNotification.Name("GoogleAdsDidDismiss"),
            object: nil
        )
    }
}

// MARK: - GADBannerViewDelegate

extension GoogleAdsManager: GADBannerViewDelegate {
    func bannerViewDidReceiveAd(_ bannerView: GADBannerView) {
        print("Banner ad loaded successfully")
        notifyAdLoaded()
    }
    
    func bannerView(_ bannerView: GADBannerView, didFailToReceiveAdWithError error: Error) {
        print("Banner ad failed to load: \(error.localizedDescription)")
        notifyAdFailedToLoad(error: error.localizedDescription)
    }
}

// MARK: - GADFullScreenContentDelegate

extension GoogleAdsManager: GADFullScreenContentDelegate {
    func adDidRecordImpression(_ ad: GADFullScreenPresentingAd) {
        print("Ad did record impression")
    }
    
    func ad(_ ad: GADFullScreenPresentingAd, didFailToPresentFullScreenContentWithError error: Error) {
        print("Ad failed to present: \(error.localizedDescription)")
        notifyAdFailedToLoad(error: error.localizedDescription)
    }
    
    func adWillPresentFullScreenContent(_ ad: GADFullScreenPresentingAd) {
        print("Ad will present full screen content")
    }
    
    func adDidDismissFullScreenContent(_ ad: GADFullScreenPresentingAd) {
        print("Ad did dismiss full screen content")
        interstitialAd = nil
        notifyAdDismissed()
    }
}
