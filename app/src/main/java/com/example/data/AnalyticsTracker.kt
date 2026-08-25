package com.example.data

interface AnalyticsTracker {
    fun packViewed(packId: String)
    fun packAddedToWhatsApp(packId: String)
    fun stickerShared(packId: String, stickerId: String)
    fun giftSent(packId: String)
    fun searchPerformed(query: String)
    fun artistSocialOpened(artistId: String, network: String)
}
