package com.lulusontime.findmyself.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.findmyself.ui.theme.FindMyselfTheme
import com.lulusontime.findmyself.wifiscan.WifiScanViewModel
import com.lulusontime.findmyself.wifiscan.repository.WifiScanRepository

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FindMyselfTheme {
                AuthenticationView(WifiScanViewModel(WifiScanRepository.getInstance()))
            }
        }
    }
}

@Composable
fun AuthenticationView(wifiScanViewModel: WifiScanViewModel = viewModel()) {

    val uiState by wifiScanViewModel.uiState.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "FindMyself", style = TextStyle(
                    fontSize = 18.sp, fontWeight = FontWeight.Bold
                )
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                value = uiState.npm,
                onValueChange = { wifiScanViewModel.changeNpm(it) },
                label = { Text("NPM") }
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 5.dp),
                onClick = {
                }) {
                Text("Login")
            }
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                onClick = { /* TODO */ }) {
                Text("Back")
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun AuthenticationPreview() {
    FindMyselfTheme {
        AuthenticationView()
    }
}