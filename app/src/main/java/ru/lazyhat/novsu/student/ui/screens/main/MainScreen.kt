package ru.lazyhat.novsu.student.ui.screens.main

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import ru.lazyhat.novsu.student.ui.activities.qrcode.ScannerContract

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val launcherCamera = rememberLauncherForActivityResult(
        contract = ScannerContract(),
        onResult = {
            Toast.makeText(context, "Registered on Lesson", Toast.LENGTH_SHORT).show()
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
            Button({
                launcherCamera.launch(Unit)
            }) {
                Text("Scan code")
            }
        }
    }
}