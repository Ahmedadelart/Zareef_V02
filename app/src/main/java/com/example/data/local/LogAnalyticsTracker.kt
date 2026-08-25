package com.example.data.local

import android.util.Log
import com.example.data.AnalyticsTracker

class LogAnalyticsTracker : AnalyticsTracker {
    private val TAG = "ZareefAnalytics"
    
    override fun packViewed(packId: String) {
        Log.d(TAG, "packViewed: $packId")
    }

    override fun packAddedToWhatsApp(packId: String) {
        Log.d(TAG, "packAddedToWhatsApp: $packId")
    }

    override fun stickerShared(packId: String, stickerId: String) {
        Log.d(TAG, "stickerShared: $packId, $stickerId")
    }

    override fun giftSent(packId: String) {
        Log.d(TAG, "giftSent: $packId")
    }

    override fun searchPerformed(query: String) {
        Log.d(TAG, "searchPerformed: $query")
    }

    override fun artistSocialOpened(artistId: String, network: String) {
        Log.d(TAG, "artistSocialOpened: $artistId, $network")
    }
}
