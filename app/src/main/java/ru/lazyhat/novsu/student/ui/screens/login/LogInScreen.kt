package ru.lazyhat.novsu.student.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.lazyhat.models.LoginStatus
import ru.lazyhat.novsu.student.data.repo.MainRepository

@Composable
fun LoginScreen(onSuccess: () -> Unit) {
    val viewModel: LoginScreenViewModel = koinViewModel()
    val logIn: () -> Unit = {
        viewModel.createEvent(LoginScreenEvent.Login(onSuccess))
    }
    val uiState by viewModel.state.collectAsState()

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Log In")
            TextField(
                uiState.username,
                { viewModel.createEvent(LoginScreenEvent.ChangeUsername(it)) },
                label = { Text("username") }
            )
            TextField(
                uiState.password,
                { viewModel.createEvent(LoginScreenEvent.ChangePassword(it)) },
                label = { Text("password") }
            )
            Text(uiState.status.description.orEmpty())
            Button(onClick = logIn) {
                Text("Log In")
            }
        }
    }
}

class LoginScreenViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginScreenState.Default)
    val state = _uiState.asStateFlow()

    fun createEvent(e: LoginScreenEvent) = onEvent(e)

    private fun onEvent(e: LoginScreenEvent): Any = when (e) {
        is LoginScreenEvent.ChangeUsername -> {
            updateState { it.copy(username = e.new, status = LoginStatus.Idle) }
        }

        is LoginScreenEvent.ChangePassword -> {
            updateState { it.copy(password = e.new, status = LoginStatus.Idle) }
        }

        is LoginScreenEvent.Login -> {
            updateState { it.copy(status = LoginStatus.Loading) }
            viewModelScope.launch {
                mainRepository.login(state.value.username, state.value.password).let {
                    if (it) {
                        e.onSuccess()
                        updateState { it.copy(status = LoginStatus.Idle) }
                    } else {
                        updateState { it.copy(status = LoginStatus.Failed) }
                    }
                }
            }
        }
    }

    private fun updateState(new: (old: LoginScreenState) -> LoginScreenState) = _uiState.update(new)
}

data class LoginScreenState(
    val username: String,
    val password: String,
    val status: LoginStatus
) {
    companion object {
        val Default = LoginScreenState(
            "",
            "",
            LoginStatus.Idle
        )
    }
}

sealed class LoginScreenEvent {
    data class ChangeUsername(val new: String) : LoginScreenEvent()
    data class ChangePassword(val new: String) : LoginScreenEvent()
    data class Login(val onSuccess: () -> Unit) : LoginScreenEvent()
}