package com.lulusontime.findmyself.websocket

import android.util.Log
import com.lulusontime.findmyself.wifiscan.WifiScanViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class MyWebsocketListener(
    private val viewModel: WifiScanViewModel
) : WebSocketListener() {


    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)

    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        viewModel.changeIsWsConnected(true)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        viewModel.reconnect()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        viewModel.reconnect()
    }
}