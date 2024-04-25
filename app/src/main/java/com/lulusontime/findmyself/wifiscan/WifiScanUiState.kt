package com.lulusontime.findmyself.wifiscan

data class WifiScanUiState(
    val npm: String = "",
    val isPermissionGranted: Boolean = false,
    val isScanning: Boolean = false,
    val isWsConnected: Boolean = false,
)