package com.lulusontime.findmyself.websocket

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class MyWebsocketListener(
    private val scope: CoroutineScope
) : WebSocketListener() {

    private val _websocketMessage = MutableStateFlow<String>("")
    val websocketMessage = _websocketMessage.asStateFlow()


    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
       scope.launch {
           _websocketMessage.emit(text)
       }
    }
}