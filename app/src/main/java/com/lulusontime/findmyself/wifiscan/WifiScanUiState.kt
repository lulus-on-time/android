package com.lulusontime.findmyself.wifiscan

data class WifiScanUiState(
    val currentLocation: String = "",
    val isScanning: Boolean = false,
    val permissionGranted: Boolean = true,
    val amtFPCollected: Int = 0,
)