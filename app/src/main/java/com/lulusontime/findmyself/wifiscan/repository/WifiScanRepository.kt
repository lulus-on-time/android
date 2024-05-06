package com.lulusontime.findmyself.wifiscan.repository

import android.content.Context
import com.google.gson.Gson
import com.lulusontime.findmyself.BuildConfig
import com.lulusontime.findmyself.websocket.FingerprintDetail
import com.lulusontime.findmyself.websocket.FingerprintModel
import com.lulusontime.findmyself.websocket.FingerprintOutwardsMessage
import com.lulusontime.findmyself.websocket.MyWebsocketListener
import com.lulusontime.findmyself.wifiscan.broadcastreceiver.WifiScanReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.logging.HttpLoggingInterceptor
import java.lang.Thread.State
import java.util.concurrent.TimeUnit

class WifiScanRepository private constructor() {

    private val gson = Gson()

    private val websocketFactory = WebSocket.Factory { request, listener ->
        val client = OkHttpClient.Builder().
        addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
            .pingInterval(40L, TimeUnit.SECONDS)
            .build()
        client.newWebSocket(request, listener);
    }

    private val request = Request.Builder().url(BuildConfig.WSURL).build()
    private val websocketListener = MyWebsocketListener(this)

    var websocket = websocketFactory.newWebSocket(request, websocketListener)

    private var reconnectionAttempt = 3

    private val _isWssConnected = MutableStateFlow(false)
    val isWssConnected = _isWssConnected as StateFlow<Boolean>

    private val _location = MutableStateFlow("")
    val location = _location as StateFlow<String>

    private val npm = MutableStateFlow("")

    fun reconnect() {
        if (reconnectionAttempt == 0) {
            _isWssConnected.value = false
            return
        }
        reconnectionAttempt -= 1
        forceReconnect()
    }

    fun forceReconnect() {
        websocket = websocketFactory.newWebSocket(request, websocketListener)
    }

    fun changeIsWssConnectedToTrue() {
        reconnectionAttempt = 3
        _isWssConnected.value = true
    }

    fun changeNpm(npm: String) {
        this.npm.value = npm
    }

    fun sendFingerprintData(fingerprintDetails: List<FingerprintDetail>) {
        websocket.send(
            gson.toJson(
                FingerprintOutwardsMessage(
                    data = FingerprintModel(
                        fingerprintDetails
                    ),
                    npm = npm.value.ifEmpty { null }
                )
            )
        )
    }

    fun setLocation(location: String) {
        _location.value = location
    }

    companion object {
        @Volatile
        var INSTANCE: WifiScanRepository? = null

        fun getInstance(): WifiScanRepository {
            return INSTANCE ?: synchronized(this) {
                return INSTANCE ?: WifiScanRepository().also {
                    INSTANCE = it
                }
            }
        }
    }
}