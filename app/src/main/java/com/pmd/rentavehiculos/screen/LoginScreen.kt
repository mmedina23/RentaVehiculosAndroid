package com.pmd.rentavehiculos.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel // IMPORTANTE
import com.pmd.rentavehiculos.viewModel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(), // Hilt inyecta el ViewModel aquí
    onLoginSuccess: (String) -> Unit
) {
    val loginResult by viewModel.loginResult.observeAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        TextField(value = username, onValueChange = { username = it }, label = { Text("Usuario") })
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )
        Button(onClick = { viewModel.login(username, password) }) {
            Text("Iniciar Sesión")
        }
        loginResult?.let {
            if (it.role == "admin") {
                onLoginSuccess("admin")
            } else {
                onLoginSuccess("cliente")
            }
        }
    }
}
