package com.lulusontime.findmyself.broadcastreceiver

import android.Manifest
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
import androidx.core.content.ContextCompat

class WifiScanReceiver(context: Context) : BroadcastReceiver() {
    val wifiManager: WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    override fun onReceive(context: Context, intent: Intent?) {
        val success = intent?.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
        if (success != true) {
            Log.e(TAG, "Wifi scan throttled")
            return
        }
        Log.i(TAG, "Wifi Scan Success")

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            Log.e(TAG, "Device version is below ${Build.VERSION_CODES.P}")
            return
        }

        val rangingRequestBuilder = RangingRequest.Builder()

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return
        }

        val scanResults = wifiManager.scanResults
        for (scanResult in scanResults) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                rangingRequestBuilder.addAccessPoint(
                    scanResult
                )
            }
            Log.i(TAG, scanResult.BSSID)
        }

        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_WIFI_RTT)) {
            Log.e(TAG, "Device does not have Wifi RTT Features")
            return
        }

        val wifiRttManager = context.getSystemService(Context.WIFI_RTT_RANGING_SERVICE) as WifiRttManager
        wifiRttManager.startRanging(
            rangingRequestBuilder.build(),
            context.mainExecutor,
            @RequiresApi(Build.VERSION_CODES.P)
            object : RangingResultCallback() {
                override fun onRangingFailure(code: Int) {
                    Log.e(TAG, "Ranging Failure")
                }

                override fun onRangingResults(results: MutableList<RangingResult>) {
                    val fingerprint = results.map { result ->
                        result.rssi
                    }
                    Log.i(TAG, "Fingerprint: $fingerprint")
                }

            }
        )
        Log.i(TAG, "Start Ranging")
    }

    companion object {
        private const val TAG = "WifiScanReceiver"
    }
}