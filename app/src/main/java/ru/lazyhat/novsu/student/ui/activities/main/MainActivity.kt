package ru.lazyhat.novsu.student.ui.activities.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import org.koin.compose.koinInject
import ru.lazyhat.novsu.student.data.repo.MainRepository
import ru.lazyhat.novsu.student.ui.screens.login.LoginScreen
import ru.lazyhat.novsu.student.ui.screens.main.MainScreen
import ru.lazyhat.novsu.student.ui.theme.StudentAppTheme

enum class LogInState {
    Loading,
    Success,
    Fail
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentAppTheme {
                val mainRepository: MainRepository = koinInject()
                var logInState by remember { mutableStateOf(LogInState.Loading) }
                val scope = rememberCoroutineScope { Dispatchers.IO }
                LaunchedEffect(key1 = Unit) {
                    logInState = if (mainRepository.checkLoggedInOrLogIn()) {
                        LogInState.Success
                    } else
                        LogInState.Fail
                }
                when (logInState) {
                    LogInState.Loading -> LoadingScreen()
                    LogInState.Success -> MainScreen()
                    LogInState.Fail -> LoginScreen {
                        logInState = LogInState.Success
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(Modifier.fillMaxSize()) {
        Text("Loading", modifier = Modifier.align(Alignment.Center))
    }
}