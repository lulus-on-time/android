package com.lulusontime.findmyself

import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.lulusontime.findmyself.wifiscan.broadcastreceiver.WifiScanReceiver
import com.lulusontime.findmyself.ui.theme.FindMyselfTheme
import com.lulusontime.findmyself.websocket.MyWebsocketListener
import com.lulusontime.findmyself.websocket.WebsocketRepository
import com.lulusontime.findmyself.wifiscan.WifiScanScreen
import com.lulusontime.findmyself.wifiscan.WifiScanViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class MainActivity : ComponentActivity() {

    private var wsr: WifiScanReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val wsListener = MyWebsocketListener(lifecycleScope)
        val request = Request.Builder().url("wss://find-myself-413712.et.r.appspot.com ").build()

        val myWebsocket = WebSocket.Factory { request, listener ->
            val client = OkHttpClient.Builder()
                .build()
            client.newWebSocket(request, listener)
        }.newWebSocket(request, wsListener)

        val repository = WebsocketRepository.getInstance(myWebsocket)

        wsr = WifiScanReceiver(this, repository)
        registerReceiver(wsr,
            IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))

        setContent {
            FindMyselfTheme {
                FindMyselfApp(
                    repository,
                    wsListener
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (wsr == null) return
        unregisterReceiver(wsr)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

@Composable
fun FindMyselfApp(
    repository: WebsocketRepository,
    wsListener: MyWebsocketListener
) {
    Scaffold (
        topBar = { FindMyselfTopAppBar()}
    ) {innerPadding ->
        WifiScanScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            wifiScanViewModel = WifiScanViewModel(repository, wsListener)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindMyselfTopAppBar(
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(title = {
        Text(text = stringResource(id = R.string.app_name))
    },
        modifier = modifier)
}