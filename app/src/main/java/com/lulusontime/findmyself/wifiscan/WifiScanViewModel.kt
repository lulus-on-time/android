package com.lulusontime.findmyself.wifiscan

import android.net.wifi.ScanResult
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import com.lulusontime.findmyself.wifiscan.model.WifiScanModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WifiScanViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WifiScanUiState())
    val uiState = _uiState.asStateFlow()

    fun changePermissionGranted(isGranted: Boolean) {
        _uiState.update { wifiScanUiState ->
            WifiScanUiState(
                wifiScanUiState.currentLocation,
                wifiScanUiState.isScanning,
                isGranted
            )
        }
    }

    fun changeLocation(location: String) {
        _uiState.update {
            it.copy(currentLocation = location)
        }
    }

    fun changeScanStatus(status: Boolean) {
        _uiState.update {
            it.copy(isScanning = status)
        }
    }
}