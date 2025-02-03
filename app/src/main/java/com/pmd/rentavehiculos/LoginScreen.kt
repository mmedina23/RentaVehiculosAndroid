package com.pmd.rentavehiculos

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pmd.rentavehiculos.data.LoginRequest
import com.pmd.rentavehiculos.data.RetrofitServiceFactory
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@Composable
fun LoginScreen(navController: NavController) {
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Renta de Vehículos", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    isLoading.value = true
                    errorMessage.value = ""

                    try {
                        val response = RetrofitServiceFactory.retrofitService()
                            .login(LoginRequest(username.value, password.value))

                        // Si el login es exitoso, navega a la pantalla principal
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }

                        // Aquí puedes guardar el token si lo necesitas
                        // val token = response.token

                    } catch (e: HttpException) {
                        errorMessage.value = "Error de autenticación: ${e.message()}"
                    } catch (e: IOException) {
                        errorMessage.value = "Error de red: Verifica tu conexión"
                    } finally {
                        isLoading.value = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading.value
        ) {
            Text("Iniciar Sesión")
        }

        if (errorMessage.value.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(errorMessage.value, color = MaterialTheme.colorScheme.error)
        }

        if (isLoading.value) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
}