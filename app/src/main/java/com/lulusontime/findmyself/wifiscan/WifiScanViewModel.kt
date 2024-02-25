package com.lulusontime.findmyself.wifiscan

import android.net.wifi.ScanResult
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lulusontime.findmyself.websocket.MyWebsocketListener
import com.lulusontime.findmyself.websocket.WebsocketRepository
import com.lulusontime.findmyself.wifiscan.model.WifiScanModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WifiScanViewModel(
    private val websocketRepository: WebsocketRepository,
    private val wsListener: MyWebsocketListener
) : ViewModel() {
    private val _uiState = MutableStateFlow(WifiScanUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            wsListener.websocketMessage.collect {
                if (it.isEmpty()) return@collect
                _uiState.update {
                    val newAmt = it.amtFPCollected + 1
                    it.copy(amtFPCollected = newAmt)
                }
            }
        }
    }

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
        viewModelScope.launch {
            websocketRepository.setLocation(location)
        }
        _uiState.update {
            it.copy(currentLocation = location, amtFPCollected = 0)
        }
    }

    fun changeScanStatus(status: Boolean) {
        _uiState.update {
            it.copy(isScanning = status)
        }
    }
}