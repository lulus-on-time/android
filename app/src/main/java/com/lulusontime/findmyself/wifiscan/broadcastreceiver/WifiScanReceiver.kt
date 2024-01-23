package com.lulusontime.findmyself.wifiscan.broadcastreceiver

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.net.wifi.rtt.RangingRequest
import android.net.wifi.rtt.RangingResult
import android.net.wifi.rtt.RangingResultCallback
import android.net.wifi.rtt.WifiRttManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.lulusontime.findmyself.wifiscan.model.WifiScanModel

class WifiScanReceiver(
    context: Context,
    private val onReceiveScanResults: (List<WifiScanModel>) -> Unit
    ) : BroadcastReceiver() {
    private val wifiManager: WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    override fun onReceive(context: Context, intent: Intent?) {
        val success = intent?.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)

        if (success != true) {
            Log.e(TAG, "Wifi scan throttled")
            return
        }

        Log.i(TAG, "Wifi Scan Received Successfully")

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                0
            )
            return
        }

        val scanResults = wifiManager.scanResults
        onReceiveScanResults(scanResults.map {
            WifiScanModel(
                it.BSSID,
                it.is80211mcResponder,
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) it.SSID else it.wifiSsid
                    .toString(),
                it.level,
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) null else wifiManager
                    .calculateSignalLevel(it.level)
            )
        })
    }

    companion object {
        private const val TAG = "WifiScanReceiver"
    }
}