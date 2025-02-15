package com.pmd.rentavehiculos.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable

@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit,
    onExit: () -> Unit
) {
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // O el color que prefieras para el fondo
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation() // Oculta la contraseña
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (usuario.isNotBlank() && contrasena.isNotBlank()) {
                        onLogin(usuario, contrasena)
                    } else {
                        // Opcional: podrías manejar campos vacíos mediante algún Snackbar o estado interno.
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Botón para salir
            Button(
                onClick = { onExit() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salir")
            }
        }
    }
}