package ru.lazyhat.novsu.student.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.lazyhat.novsu.student.data.repo.MainRepository

@Composable
fun LoginScreen() {
    Box {
        Column(
            Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Log In")
            TextField()
        }
    }
}

class LoginScreenViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginScreenState("", ""))
    val state = _uiState.asStateFlow()

    private fun onEvent(e: LoginScreenEvent): Any? = when (e) {
        is LoginScreenEvent.ChangeUsername -> {
            updateState { it.copy(username = e.new) }
        }

        is LoginScreenEvent.ChangePassword -> {
            updateState { it.copy(username = e.new) }
        }

        LoginScreenEvent.Login -> TODO()
    }

    private fun updateState(new: (old: LoginScreenState) -> LoginScreenState) = _uiState.update(new)
}

data class LoginScreenState(
    val username: String,
    val password: String
)

sealed class LoginScreenEvent {
    data class ChangeUsername(val new: String) : LoginScreenEvent()
    data class ChangePassword(val new: String) : LoginScreenEvent()
    data object Login : LoginScreenEvent()
}