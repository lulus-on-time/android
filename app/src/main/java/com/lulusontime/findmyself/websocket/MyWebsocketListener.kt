package com.lulusontime.findmyself.websocket

import com.lulusontime.findmyself.wifiscan.repository.WifiScanRepository
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class MyWebsocketListener(
    private val repository: WifiScanRepository
) : WebSocketListener() {


    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        repository.setLocation(text)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        repository.changeIsWssConnectedToTrue()
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        repository.reconnect()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        repository.reconnect()
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        repository.reconnect()
    }
}