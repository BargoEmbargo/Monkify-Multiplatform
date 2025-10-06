import SwiftUI
import FirebaseCore

@main
struct iOSApp: App {
    
    init() {
        FirebaseApp.configure()
        // Initialize Google Ads Manager
        _ = GoogleAdsManager.shared
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
