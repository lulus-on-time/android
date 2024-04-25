package com.lulusontime.findmyself.websocket

data class FingerprintModel(
    val fingerprints: List<FingerprintDetail>
)

data class FingerprintDetail(
    val rssi: Int,
    val bssid: String
)

data class FingerprintOutwardsMessage(
    val reason: String = "fingerprint",
    val data: FingerprintModel,
    val npm: String?,
)