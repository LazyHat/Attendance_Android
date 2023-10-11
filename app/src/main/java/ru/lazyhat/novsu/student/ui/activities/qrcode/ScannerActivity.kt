package ru.lazyhat.novsu.student.ui.activities.qrcode

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import ru.lazyhat.models.RegisterStatus
import ru.lazyhat.novsu.student.data.repo.MainRepository
import ru.lazyhat.novsu.student.ui.theme.StudentAppTheme

class ScannerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentAppTheme {
                var status by remember { mutableStateOf(RegisterStatus.Idle) }
                val mainRepository = koinInject<MainRepository>()
                val context = LocalContext.current
                val lifecycleOwner = LocalLifecycleOwner.current
                val scope = rememberCoroutineScope()
                var token by remember { mutableStateOf<String?>(null) }
                LaunchedEffect(key1 = token){
                    token?.let {
                        status = RegisterStatus.Loading
                        scope.launch {
                            mainRepository.registerToLesson(it).let {
                                if (it) {
                                    status = RegisterStatus.Idle
                                    this@ScannerActivity.setResult(Activity.RESULT_OK)
                                    this@ScannerActivity.finish()
                                } else {
                                    status = RegisterStatus.Failed
                                }
                            }
                        }
                    }
                }

                val cameraProviderFuture = remember {
                    ProcessCameraProvider.getInstance(context)
                }
                var hasCameraPermission by remember {
                    mutableStateOf(
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                }
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = {
                        if (it)
                            hasCameraPermission = true
                        else {
                            this@ScannerActivity.setResult(Activity.RESULT_CANCELED)
                            this@ScannerActivity.finish()
                        }
                    })
                LaunchedEffect(key1 = Unit) {
                    launcher.launch(Manifest.permission.CAMERA)
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        if (hasCameraPermission) {
                            AndroidView(
                                factory = { context ->
                                    val previewView = PreviewView(context)
                                    val preview = Preview.Builder().build()
                                    val selector = CameraSelector.Builder()
                                        .requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
                                    preview.setSurfaceProvider(previewView.surfaceProvider)
                                    val imageAnalysis = ImageAnalysis.Builder()
                                        .setTargetResolution(
                                            Size(
                                                previewView.width,
                                                previewView.height
                                            )
                                        )
                                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                        .build()
                                    imageAnalysis.setAnalyzer(
                                        ContextCompat.getMainExecutor(context),
                                        QrCodeAnalyzer {
                                            if(status == RegisterStatus.Idle)
                                                token = it
                                        }
                                    )
                                    try {
                                        cameraProviderFuture.get().bindToLifecycle(
                                            lifecycleOwner,
                                            selector,
                                            preview,
                                            imageAnalysis
                                        )
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    previewView
                                },
                                modifier = Modifier.weight(1f)
                            )
                            status.description?.let {
                                Text(it)
                            }
                        }
                    }
                }
            }
        }
    }
}