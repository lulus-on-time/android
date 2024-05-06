package com.lulusontime.findmyself.wifiscan

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lulusontime.findmyself.websocket.MyWebsocketListener
import com.lulusontime.findmyself.wifiscan.broadcastreceiver.WifiScanReceiver
import com.lulusontime.findmyself.wifiscan.service.WifiScanService
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
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.RequestPermission(), onResult = {isGranted ->
        wifiScanViewModel.changePermissionGranted(isGranted)
    })

    fun checkPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                ctx,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                when {
                    ContextCompat.checkSelfPermission(
                        ctx,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        // You can use the API that requires the permission.
                        wifiScanViewModel.changePermissionGranted(true)
                    }
                    else -> {
                        // You can directly ask for the permission.
                        // The registered ActivityResultCallback gets the result of this request.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            launcher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        }
                    }
                }
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
        }, enabled = !uiState.isWsConnected)
        if (!uiState.isWsConnected) {
            Text("Max reconnection attempt done, please reconnect manually")
            Button(onClick = { wifiScanViewModel.forceReconnect() }) {
                Text("Reconnect manually")
            }
        }
        LazyColumn(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            items(uiState.location) {
                Text(text=it)
                Divider(thickness = 1.dp, color = Color.Red)
            }
        }
    }

    SideEffect {
        checkPermissions()
    }

    if (uiState.isWsConnected && uiState.isPermissionGranted) {
        DisposableEffect(Unit) {

            val intent = Intent(ctx, WifiScanService::class.java)
            ctx.startService(intent)

            onDispose {
                ctx.stopService(intent)
            }
        }
    }
}