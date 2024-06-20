package com.lulusontime.findmyself.wifiscan.service

import android.Manifest
import android.app.ForegroundServiceStartNotAllowedException
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Process
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.lulusontime.findmyself.CHANNEL_ID
import com.lulusontime.findmyself.CHANNEL_NAME
import com.lulusontime.findmyself.FOREGROUND_SERVICE_WIFI_SCAN_NOTIFICATION_ID
import com.lulusontime.findmyself.wifiscan.broadcastreceiver.WifiScanReceiver
import com.lulusontime.findmyself.wifiscan.repository.WifiScanRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WifiScanService : Service() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)
    private var wsr: WifiScanReceiver? = null

    private fun startForeground() {
        val locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (locationPermission == PackageManager.PERMISSION_DENIED) {
            stopSelf()
            return
        }

        val bgLocationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_BACKGROUND_LOCATION)
        } else {
            PackageManager.PERMISSION_GRANTED
        }
        if (bgLocationPermission == PackageManager.PERMISSION_DENIED) {
            stopSelf()
            return
        }

        try {
            val notifChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            val notifManager = getSystemService(NotificationManager::class.java)
            notifManager?.createNotificationChannel(notifChannel)
            val notif = NotificationCompat.Builder(this, CHANNEL_ID).build()

            ServiceCompat.startForeground(this, FOREGROUND_SERVICE_WIFI_SCAN_NOTIFICATION_ID,
                notif, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION else 0)

            val wifiManager = getSystemService(WifiManager::class.java)
            wsr = WifiScanReceiver(this, WifiScanRepository.getInstance())
            registerReceiver(wsr, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION), RECEIVER_NOT_EXPORTED)
            scope.launch {
                while(true) {
                    wifiManager?.startScan()
                    delay(1000)
                }
            }
        } catch (e: Exception) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && e is ForegroundServiceStartNotAllowedException
            ) {
                // App not in a valid state to start foreground service
                // (e.g. started from bg)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (wsr == null) return
        unregisterReceiver(wsr)
    }
}