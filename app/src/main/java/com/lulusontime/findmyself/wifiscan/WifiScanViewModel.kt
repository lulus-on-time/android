package com.lulusontime.findmyself.wifiscan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lulusontime.findmyself.websocket.FingerprintDetail
import com.lulusontime.findmyself.websocket.FingerprintModel
import com.lulusontime.findmyself.websocket.FingerprintOutwardsMessage
import com.lulusontime.findmyself.wifiscan.repository.WifiScanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WifiScanViewModel(
    private val repository: WifiScanRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WifiScanUiState())
    val uiState = _uiState as StateFlow<WifiScanUiState>

    init {
        viewModelScope.launch {
            repository.isWssConnected.collect { isWssConnected ->
                _uiState.update {
                    it.copy(isWsConnected = isWssConnected)
                }
            }
        }
    }

    fun changePermissionGranted(isGranted: Boolean) {
        _uiState.update {
            it.copy(
                isPermissionGranted = isGranted,
                npm = it.npm,
            )
        }
    }

    fun changeNpm(npm: String) {
        _uiState.update {
            it.copy(
                isPermissionGranted = it.isPermissionGranted,
                npm = npm,
            )
        }
        repository.changeNpm(npm)
    }

    fun forceReconnect() {
        repository.forceReconnect()
    }

}