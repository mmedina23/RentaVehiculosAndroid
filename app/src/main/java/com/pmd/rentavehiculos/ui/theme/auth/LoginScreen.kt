@file:OptIn(ExperimentalMaterial3Api::class)

package com.pmd.rentavehiculos.ui.auth

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.data.repository.SessionManager
import com.pmd.rentavehiculos.ui.theme.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    context: Context
) {
    val sessionManager = remember { SessionManager(context) }

    val nombreUsuario = remember { mutableStateOf("") }
    val contrasena = remember { mutableStateOf("") }

    val loginResult by authViewModel.loginResult.observeAsState()
    val errorMessage by authViewModel.errorMessage.observeAsState()

    // 🎨 Establecemos el fondo de pantalla
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.futuristic_neon_car), // Reemplaza con tu imagen
            contentDescription = "Fondo de Login",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Capa semi-transparente para mejorar legibilidad
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Iniciar Sesión",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Casilla de nombre de usuario con transparencia
            TextField(
                value = nombreUsuario.value,
                onValueChange = { nombreUsuario.value = it },
                label = { Text("Nombre de usuario", color = Color.White) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, // 🔹 Fondo completamente transparente
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White, // Texto blanco
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White, // Cursor blanco
                    focusedIndicatorColor = Color.White, // 🔹 Borde blanco cuando está enfocado
                    unfocusedIndicatorColor = Color.White // 🔹 Borde blanco cuando no está enfocado
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color.Transparent, shape = RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Casilla de contraseña con transparencia
            TextField(
                value = contrasena.value,
                onValueChange = { contrasena.value = it },
                label = { Text("Contraseña", color = Color.White) },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, // 🔹 Fondo completamente transparente
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White, // Texto blanco
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White, // Cursor blanco
                    focusedIndicatorColor = Color.White, // 🔹 Borde blanco cuando está enfocado
                    unfocusedIndicatorColor = Color.White // 🔹 Borde blanco cuando no está enfocado
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color.Transparent, shape = RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de inicio de sesión con diseño futurista
            Button(
                onClick = { authViewModel.login(nombreUsuario.value, contrasena.value) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A90E2), // Azul futurista
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Iniciar sesión")
            }

            // Mostrar errores
            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 16.dp))
            }

            // Navegar a la pantalla principal después del login
            loginResult?.let { result ->
                sessionManager.token = result.llave
                sessionManager.personaId = result.persona?.id
                sessionManager.perfil = result.perfil

                LaunchedEffect(Unit) {
                    val destination = if (result.perfil == "ADMIN") "admin_home" else "cliente_home"
                    navController.navigate(destination) {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    }
}
