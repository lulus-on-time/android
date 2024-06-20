package com.lulusontime.findmyself.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.lulusontime.findmyself.auth.AuthActivity
import com.lulusontime.findmyself.R
import com.lulusontime.findmyself.wifiscan.WifiScanViewModel
import com.lulusontime.findmyself.wifiscan.service.WifiScanService
import kotlinx.coroutines.launch

@Composable
fun MapScreen(mapViewModel: MapViewModel, wifiScanViewModel: WifiScanViewModel) {
    val mapUiState by mapViewModel.uiState.collectAsState()
    val wifiScanUiState by wifiScanViewModel.uiState.collectAsState()

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.RequestPermission(), onResult = { isGranted ->
        wifiScanViewModel.changePermissionGranted(isGranted)
    })
    val displayMetrics = LocalContext.current.resources.displayMetrics

    val coroutineScope = rememberCoroutineScope()
    val hScroll = rememberScrollState()
    val vScroll = rememberScrollState()

    val myLocScrollValue = listOf(
        ((mapUiState.myLoc[1] ?: 0.0) * mapUiState.scale + 125 - displayMetrics.widthPixels / 2)
            .toInt(),
        ((mapUiState.geojsonResponse.floor.maxY - (mapUiState.myLoc[0])) * mapUiState.scale +
                125 -
                displayMetrics
            .heightPixels / 2)
            .toInt()
    )

    fun checkPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                when {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        // You can use the API that requires the permission.
                        wifiScanViewModel.changePermissionGranted(true)
                    }
                    else -> {
                        // You can directly ask for the permission.
                        // The registered ActivityResultCallback gets the result of this request.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            launcher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        }
                    }
                }
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    Scaffold(
        bottomBar = {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "FindMyself",
                        modifier = Modifier.padding(16.dp),
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    IconButton(onClick = {
                        context.startActivity(
                            Intent(
                                context,
                                AuthActivity::class.java
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Authentication",
                        )
                    }
                }
                Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)
                Text(
                    text = "Lantai " + mapUiState.geojsonResponse.floor.name,
                    modifier = Modifier.padding(16.dp),
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                coroutineScope.launch {
                    hScroll.animateScrollTo(myLocScrollValue[0])
                    vScroll.animateScrollTo(myLocScrollValue[1])
                }
            }) {
                Icon(painter = painterResource(R.drawable.baseline_my_location_24), contentDescription = "My Location")
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(vScroll)
                .horizontalScroll(hScroll)
                .padding(innerPadding),
            color = Color.LightGray.copy(alpha = 0.2f)
        ) {
            if (mapUiState.isMapReady) {
                FloorPlanView(modifier = Modifier.fillMaxSize(), geoJsonResponse = mapUiState.geojsonResponse, scale =
                mapUiState.scale, myLoc = mapUiState.myLoc)
            } else {
                CircularProgressIndicator()
            }

        }
    }

    if (mapUiState.isMapReady) {
        LaunchedEffect(Unit) {
            hScroll.animateScrollTo(myLocScrollValue[0])
            vScroll.animateScrollTo(myLocScrollValue[1])
        }
    }

    SideEffect {
        checkPermissions()
    }

    if (wifiScanUiState.isWsConnected && wifiScanUiState.isPermissionGranted) {
        DisposableEffect(Unit) {

            val intent = Intent(context, WifiScanService::class.java)
            context.startService(intent)

            onDispose {
                context.stopService(intent)
            }
        }
    }
}