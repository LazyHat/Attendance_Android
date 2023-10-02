package ru.lazyhat.novsu.student.ui.screens.main

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.lazyhat.novsu.student.ui.activities.qrcode.ScannerContract

@Composable
fun MainScreen() {
    var text by remember {
        mutableStateOf<String?>(null)
    }
    val launcherCamera =
        rememberLauncherForActivityResult(contract = ScannerContract(), onResult = {
            text = it
        })
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text.toString())
            Button({
                launcherCamera.launch(Unit)
            }) {
                Text("Scan code")
            }
        }
    }
}