package com.lulusontime.findmyself.wifiscan

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lulusontime.findmyself.websocket.MyWebsocketListener
import com.lulusontime.findmyself.wifiscan.broadcastreceiver.WifiScanReceiver
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

@Composable
fun WifiScanScreen(
    modifier: Modifier = Modifier,
    wifiScanViewModel: WifiScanViewModel = viewModel(),
) {
    val uiState by wifiScanViewModel.uiState.collectAsState()

    val ctx = LocalContext.current
    val wifiManager = ctx.getSystemService(WifiManager::class.java)
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.RequestPermission(), onResult = {isGranted ->
        wifiScanViewModel.changePermissionGranted(isGranted)
    })
    val wsr = WifiScanReceiver(ctx, wifiScanViewModel)

    fun checkPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                ctx,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                wifiScanViewModel.changePermissionGranted(true)
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Permissions Granted: ${uiState.isPermissionGranted}")
        if (!uiState.isPermissionGranted) {
            Button(onClick = {
                checkPermissions()
            }) {
                Text("Enable Permissions")
            }
        }
        TextField(value = uiState.npm, onValueChange = {
            wifiScanViewModel.changeNpm(it);
        })
        Button(onClick = {
            wifiScanViewModel.changeScanStatus(toggle = true)
        }, enabled = uiState.isWsConnected) {
            Text(text = if (uiState.isScanning) "Stop Scanning" else "Start Scanning")
        }
        if (!uiState.isWsConnected) {
            Text("Max reconnection attempt done, please reconnect manually")
            Button(onClick = { wifiScanViewModel.forceReconnect() }) {
                Text("Reconnect manually")
            }
        }
    }

    DisposableEffect(ctx) {

        ctx.registerReceiver(wsr, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))

        onDispose {
            ctx.unregisterReceiver(wsr)
        }
    }

    if (uiState.isScanning) {
        LaunchedEffect(Unit) {
            while (uiState.isScanning) {
                delay(2000)
                wifiManager.startScan()
            }
        }
    }

    SideEffect {
        checkPermissions()
    }
}