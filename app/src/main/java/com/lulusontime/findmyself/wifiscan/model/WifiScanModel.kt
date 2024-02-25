package com.lulusontime.findmyself.wifiscan.model

import android.net.wifi.WifiSsid

data class WifiScanModel(
    val bssid: String,
    val supportWifiRanging: Boolean,
    val ssid: String,
    val rawSignalLevel: Int,
    val signalLevel: Int?,
)
