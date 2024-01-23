package com.lulusontime.findmyself.wifiscan

import android.net.wifi.ScanResult
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import com.lulusontime.findmyself.wifiscan.model.WifiScanModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WifiScanViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(emptyList<WifiScanModel>())
    val uiState = _uiState.asStateFlow()

    fun addWifiScans(wifiScans: List<WifiScanModel>) {
        _uiState.value = _uiState.value + wifiScans
    }
}