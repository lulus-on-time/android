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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lulusontime.findmyself.broadcastreceiver.WifiScanReceiver
import com.lulusontime.findmyself.ui.theme.FindMyselfTheme

class MainActivity : ComponentActivity() {

    private lateinit var wifiScanReceiver: WifiScanReceiver

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        for ((permission, granted) in permissions) {
            if (!granted) {
                Log.e(TAG, "$permission not granted.")
                return@registerForActivityResult
            }
        }
        Log.i(TAG, "All Permissions Granted")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FindMyselfTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }

        checkForPermissions()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            Log.e(TAG, "Device Version Lower than R")
            return
        }
        wifiScan()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::wifiScanReceiver.isInitialized) {
            unregisterReceiver(wifiScanReceiver)
            Log.i(TAG, "UnRegister Wifi Scan Receiver")

        }
    }

    private fun checkForPermissions() {
        val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.NEARBY_WIFI_DEVICES
        ) else arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )

        requestPermissionLauncher.launch(requiredPermissions)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun wifiScan() {
        wifiScanReceiver = WifiScanReceiver(this)

        registerReceiver(
            wifiScanReceiver,
            IntentFilter().apply {
                addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            }
        )
        Log.i(TAG, "Register Wifi Scan Receiver")


        val wifiManager: WifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.startScan()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FindMyselfTheme {
        Greeting("Android")
    }
}