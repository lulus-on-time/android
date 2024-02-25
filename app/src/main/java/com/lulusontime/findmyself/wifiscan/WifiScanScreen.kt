package com.lulusontime.findmyself.wifiscan

import android.Manifest
import android.app.Activity
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lulusontime.findmyself.R
import com.lulusontime.findmyself.ui.theme.FindMyselfTheme
import com.lulusontime.findmyself.wifiscan.broadcastreceiver.WifiScanReceiver
import com.lulusontime.findmyself.wifiscan.model.WifiScanModel
import kotlinx.coroutines.delay

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

    fun checkPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                ctx,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                wifiScanViewModel.changeScanStatus(true)
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (uiState.isScanning)"Scanning at ${uiState.
                currentLocation}" else "No Location Set."
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = uiState.currentLocation, onValueChange =
        wifiScanViewModel::changeLocation,
            enabled = !uiState.isScanning)
        ElevatedButton(onClick = {
            if (uiState.isScanning) {
                wifiScanViewModel.changeScanStatus(false)
            } else {
                wifiScanViewModel.changeScanStatus(true)
                checkPermissions()
            }
        }) {
            Text(
                text = if (uiState.isScanning) "Stop Scan" else "Start Scan"
            )
        }
        if (!uiState.permissionGranted) {
            Text(
                text =  "One or more permissions not granted. Click here or go to settings to " +
                        "grant it.",
                modifier = Modifier.clickable {
                    launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

                }
            )
        }
        LaunchedEffect(key1 = uiState.isScanning) {
            if (uiState.isScanning) {
                while (true) {
                    wifiManager.startScan()
                    delay(3000)
                }
            }
        }
    }
}