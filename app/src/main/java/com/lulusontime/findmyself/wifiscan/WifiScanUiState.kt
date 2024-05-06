package com.lulusontime.findmyself.wifiscan

data class WifiScanUiState(
    val npm: String = "",
    val isPermissionGranted: Boolean = false,
    val isWsConnected: Boolean = false,
    val location: List<String> = emptyList(),
)