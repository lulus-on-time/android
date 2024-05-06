package com.lulusontime.findmyself

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.lulusontime.findmyself.wifiscan.broadcastreceiver.WifiScanReceiver
import com.lulusontime.findmyself.ui.theme.FindMyselfTheme
import com.lulusontime.findmyself.wifiscan.WifiScanScreen
import com.lulusontime.findmyself.wifiscan.WifiScanViewModel
import com.lulusontime.findmyself.wifiscan.repository.WifiScanRepository
import com.lulusontime.findmyself.wifiscan.service.WifiScanService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FindMyselfTheme {
                FindMyselfApp(
                )
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

@Composable
fun FindMyselfApp(
) {
    Scaffold (
        topBar = { FindMyselfTopAppBar()}
    ) {innerPadding ->
        WifiScanScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            wifiScanViewModel = WifiScanViewModel(WifiScanRepository.getInstance())
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