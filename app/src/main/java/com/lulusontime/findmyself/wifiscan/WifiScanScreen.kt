package com.lulusontime.findmyself.wifiscan

import android.app.Activity
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.util.Log
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lulusontime.findmyself.R
import com.lulusontime.findmyself.ui.theme.FindMyselfTheme
import com.lulusontime.findmyself.wifiscan.broadcastreceiver.WifiScanReceiver
import com.lulusontime.findmyself.wifiscan.model.WifiScanModel

@Composable
fun WifiScanScreen(
    modifier: Modifier = Modifier,
    wifiScanViewModel: WifiScanViewModel = viewModel()
) {
    val wifiScans by wifiScanViewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = dimensionResource(id = R.dimen.medium_space),
            vertical = dimensionResource(id = R.dimen.small_space)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.medium_space))
    ) {
        items(wifiScans) {wifiScan ->
            WifiScanItem(wifiScanModel = wifiScan,
                modifier = Modifier.fillMaxWidth())
        }
    }

    val ctx = LocalContext.current
    val wifiScanReceiver = WifiScanReceiver(ctx, wifiScanViewModel::addWifiScans)
    ctx.registerReceiver(
        wifiScanReceiver,
        IntentFilter().apply { addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) }
    )

    (ctx as Activity).requestPermissions(
        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
        0
    )

    ctx.getSystemService(WifiManager::class.java)?.startScan()
    Log.i("WifiScanScreen", "Wifi Scan Started")

}

@Composable
fun WifiScanItem(
    modifier: Modifier = Modifier,
    wifiScanModel: WifiScanModel
) {
    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.medium_space)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = wifiScanModel.ssid,
                    style = MaterialTheme.typography.displaySmall
                )
                Spacer(modifier = Modifier
                    .height(dimensionResource(id = R.dimen.small_space)))
                Text(
                    text = wifiScanModel.bssid,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.large_space)))
            Column {
                Text(
                    text = stringResource(
                    id = R.string.show_raw_rssi,
                    wifiScanModel.rawSignalLevel
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(
                    modifier = Modifier
                        .height(
                            dimensionResource(
                                id = R.dimen.medium_space
                            )
                        )
                )
                wifiScanModel.signalLevel?.let {
                    Text(
                        text = stringResource(
                            id = R.string.show_signal_level,
                            wifiScanModel.signalLevel ?: 0
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = if(wifiScanModel.supportWifiRanging) Icons.Filled.Check else Icons
                    .Filled.Close,
                tint = if (wifiScanModel.supportWifiRanging) Color.Green else Color.Red,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun WifiScanItemPreview() {
    FindMyselfTheme {
        WifiScanItem(
            wifiScanModel = WifiScanModel(
                bssid = "21:AC:09:11:F1:A2",
                supportWifiRanging = false,
                ssid = "Wifi Rumah",
                rawSignalLevel = -90,
                signalLevel = 200,
                ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun WifiScanScreenPreview() {
    FindMyselfTheme {
        WifiScanScreen()
    }
}