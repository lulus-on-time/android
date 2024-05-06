package com.lulusontime.findmyself.wifiscan.broadcastreceiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import androidx.core.content.getSystemService
import com.google.gson.Gson
import com.lulusontime.findmyself.websocket.FingerprintDetail
import com.lulusontime.findmyself.websocket.FingerprintOutwardsMessage
import com.lulusontime.findmyself.wifiscan.WifiScanViewModel
import com.lulusontime.findmyself.wifiscan.repository.WifiScanRepository
import okhttp3.WebSocket

class WifiScanReceiver(
    context: Context,
    var repository: WifiScanRepository
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

        val fingerprintDetails = scanResults.map { result ->

            if (Build.VERSION.SDK_INT < 33) {
                FingerprintDetail(result.level, result.BSSID.uppercase())
            } else {
                FingerprintDetail(result.level, result.BSSID.uppercase())
            }
        }

        Log.i("Wifi Scan Receiver", fingerprintDetails.toString())

        repository.sendFingerprintData(fingerprintDetails)
    }

    companion object {
        private const val TAG = "WifiScanReceiver"
    }
}