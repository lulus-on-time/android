package com.lulusontime.findmyself.websocket

data class FingerprintModel(
    val location: String,
    val fingerprints: List<FingerprintDetail>
)

data class FingerprintDetail(
    val rssi: Int,
    val bssid: String
)

data class FingerprintWebsocketBody(
    val reason: String = "fingerprint",
    val data: FingerprintModel
)