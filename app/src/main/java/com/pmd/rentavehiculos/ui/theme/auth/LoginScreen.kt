package com.pmd.rentavehiculos.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.ui.theme.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val nombreUsuario = remember { mutableStateOf("") }
    val contrasena = remember { mutableStateOf("") }

    val loginResult by authViewModel.loginResult.observeAsState()
    val errorMessage by authViewModel.errorMessage.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = nombreUsuario.value,
            onValueChange = { nombreUsuario.value = it },
            label = { Text("Nombre de usuario") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = contrasena.value,
            onValueChange = { contrasena.value = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            authViewModel.login(nombreUsuario.value, contrasena.value)
        }) {
            Text("Iniciar sesión")
        }

        // Mostrar errores
        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 16.dp))
        }

        // Navegar a la pantalla principal después del login
        loginResult?.let { result ->
            val llaveApi = result.llave

            LaunchedEffect(Unit) {
                if (result.perfil == "ADMIN") {
                    navController.navigate("admin_home/$llaveApi") // Asegúrate de que esta ruta coincida
                } else if (result.perfil == "CLIENTE") {
                    navController.navigate("cliente_home") // Agrega esta ruta si es necesaria
                }
            }
        }
    }
}

