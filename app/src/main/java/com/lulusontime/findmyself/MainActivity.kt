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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lulusontime.findmyself.wifiscan.broadcastreceiver.WifiScanReceiver
import com.lulusontime.findmyself.ui.theme.FindMyselfTheme
import com.lulusontime.findmyself.wifiscan.WifiScanScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FindMyselfTheme {
                FindMyselfApp()
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

@Composable
fun FindMyselfApp() {
    Scaffold (
        topBar = { FindMyselfTopAppBar()}
    ) {innerPadding ->
        WifiScanScreen(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindMyselfTopAppBar(
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(title = {
        Text(text = stringResource(id = R.string.app_name))
    },
        modifier = modifier)
}