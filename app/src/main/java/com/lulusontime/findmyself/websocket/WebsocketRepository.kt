package com.lulusontime.findmyself.websocket

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.launch
import okhttp3.WebSocket

class WebsocketRepository private constructor(
    private val myWebsocket: WebSocket
){
    private var _location = ""

    fun sendFingerprintData(fingerprints: List<FingerprintDetail>) {
        CoroutineScope(Dispatchers.IO).launch {
            val body = FingerprintWebsocketBody(data = FingerprintModel(_location, fingerprints))
            val gson = Gson()
            myWebsocket.send(gson.toJson(body))
        }
    }

    suspend fun setLocation(location: String) {
        _location = location
    }


    companion object {
        @Volatile
        private var INSTANCE: WebsocketRepository? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(ws: WebSocket): WebsocketRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: WebsocketRepository(ws).also { INSTANCE = it }
            }
        }
    }
}