package com.lulusontime.findmyself

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.findmyself.ui.theme.FindMyselfTheme
import com.lulusontime.findmyself.map.FloorPlanView
import com.lulusontime.findmyself.map.MapScreen
import com.lulusontime.findmyself.map.MapViewModel
import com.lulusontime.findmyself.map.data.FloorRepository
import com.lulusontime.findmyself.map.data.retrofit.ApiConfig
import com.lulusontime.findmyself.wifiscan.broadcastreceiver.WifiScanReceiver
import com.lulusontime.findmyself.wifiscan.WifiScanScreen
import com.lulusontime.findmyself.wifiscan.WifiScanViewModel
import com.lulusontime.findmyself.wifiscan.repository.WifiScanRepository
import com.lulusontime.findmyself.wifiscan.service.WifiScanService
import kotlinx.coroutines.launch

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
    MapScreen(mapViewModel = MapViewModel(WifiScanRepository.getInstance(), FloorRepository
        .getInstance(ApiConfig.getFloorApiConfig())),
        wifiScanViewModel = WifiScanViewModel(
        WifiScanRepository.getInstance())
    )


}