package com.pmd.rentavehiculos.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmd.rentavehiculos.viewmodel.LoginState
import com.pmd.rentavehiculos.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: (String, String) -> Unit // Acción al iniciar sesión correctamente
) {
    val viewModel: LoginViewModel = viewModel()
    var nombreUsuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = nombreUsuario,
            onValueChange = { nombreUsuario = it },
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.iniciarSesion(nombreUsuario, contrasena)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar sesión")
        }

        // Manejar el estado del login
        when (loginState) {
            is LoginState.Loading -> Text("Cargando...")
            is LoginState.Success -> {
                val successState = loginState as LoginState.Success
                Text(successState.mensaje)
                // Llamamos a la función de redirección al éxito
                onLoginSuccess(successState.perfil, successState.llave)
            }
            is LoginState.Error -> Text("Error: ${(loginState as LoginState.Error).error}")
        }
    }
}
