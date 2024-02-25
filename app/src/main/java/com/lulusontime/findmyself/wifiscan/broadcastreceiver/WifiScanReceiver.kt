package com.lulusontime.findmyself.wifiscan.broadcastreceiver

import android.Manifest
import android.annotation.SuppressLint
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
    ) : BroadcastReceiver() {
    private val wifiManager: WifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent?) {
        val success = intent?.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)

        if (success != true) {
            Log.e(TAG, "Wifi scan throttled")
            return
        }

        Log.i(TAG, "Wifi Scan Received Successfully")

        val scanResults = wifiManager.scanResults


    }

    companion object {
        private const val TAG = "WifiScanReceiver"
    }
}