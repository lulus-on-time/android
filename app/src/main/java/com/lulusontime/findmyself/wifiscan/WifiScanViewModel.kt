package com.lulusontime.findmyself.wifiscan

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.lulusontime.findmyself.BuildConfig
import com.lulusontime.findmyself.websocket.FingerprintDetail
import com.lulusontime.findmyself.websocket.FingerprintModel
import com.lulusontime.findmyself.websocket.FingerprintOutwardsMessage
import com.lulusontime.findmyself.websocket.MyWebsocketListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.logging.HttpLoggingInterceptor

class WifiScanViewModel(
) : ViewModel() {

    private val websocketFactory = WebSocket.Factory { request, listener ->
        val client = OkHttpClient.Builder().
        addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
            .build()
        client.newWebSocket(request, listener);
    }

    private val request = Request.Builder().url(BuildConfig.WSURL).build()
    private val websocketListener = MyWebsocketListener(this)

    var websocket = websocketFactory.newWebSocket(request, websocketListener)

    private val _uiState = MutableStateFlow(WifiScanUiState())
    val uiState = _uiState as StateFlow<WifiScanUiState>

    private val gson = Gson()

    private var reconnectionAttempt = 3

    fun changePermissionGranted(isGranted: Boolean) {
        _uiState.update {
            it.copy(
                isPermissionGranted = isGranted,
                npm = it.npm,
                isScanning = it.isScanning
            )
        }
    }

    fun changeNpm(npm: String) {
        _uiState.update {
            it.copy(
                isPermissionGranted = it.isPermissionGranted,
                npm = npm,
                isScanning = it.isScanning
            )
        }
    }

    fun changeScanStatus(status: Boolean = false , toggle: Boolean = false) {
        if (toggle) {
            _uiState.update {
                it.copy(
                    isPermissionGranted = it.isPermissionGranted,
                    npm = it.npm,
                    isScanning = !it.isScanning
                )
            }
            return
        } else {
            _uiState.update {
                it.copy(
                    isPermissionGranted = it.isPermissionGranted,
                    npm = it.npm,
                    isScanning = status
                )
            }
        }
    }

    fun reconnect() {
        if (reconnectionAttempt == 0) {
            changeIsWsConnected(false)
            return
        }
        reconnectionAttempt -= 1
        forceReconnect()
    }

    fun forceReconnect() {
        websocket = websocketFactory.newWebSocket(request, websocketListener)
    }

    fun sendFingerprintData(fingerprintDetails: List<FingerprintDetail>) {
        websocket.send(
            gson.toJson(
                FingerprintOutwardsMessage(
                    data = FingerprintModel(
                        fingerprintDetails
                    ),
                    npm = _uiState.value.npm.ifEmpty { null }
                )
            )
        )
    }

    fun changeIsWsConnected(isConnected: Boolean) {
        _uiState.update {
            it.copy(
                isWsConnected = isConnected,
                isScanning = false,

            )
        }

        if (isConnected) {
            reconnectionAttempt = 3
        }
    }

}