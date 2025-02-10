package com.pmd.rentavehiculos.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.pmd.rentavehiculos.models.LoginRequest
import com.pmd.rentavehiculos.models.LoginResponse
import com.pmd.rentavehiculos.network.ApiClient
import com.pmd.rentavehiculos.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen()
        }
    }
}

@Composable
fun LoginScreen() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val apiService = ApiClient.retrofit.create(ApiService::class.java)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Iniciar Sesión", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        if (loginError != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = loginError!!, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    isLoading = true
                    login(apiService, username, password) { success, role, errorMessage ->
                        isLoading = false
                        if (success) {
                            val intent = if (role == "admin") {
                                Intent(context, AdminActivity::class.java)
                            } else {
                                Intent(context, ClienteActivity::class.java)
                            }
                            context.startActivity(intent)
                        } else {
                            loginError = errorMessage ?: "Error al iniciar sesión"
                        }
                    }
                } else {
                    Toast.makeText(context, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text("Iniciar Sesión")
            }
        }
    }
}
fun login(
    apiService: ApiService,
    username: String,
    password: String,
    onResult: (Boolean, String?, String?) -> Unit
) {
    val request = LoginRequest(username, password)

    // 🔥 DEBUG: Ver qué se envía
    println("📤 Enviando solicitud de login: ${request.nombreUsuario} - ${request.contrasena}")
    println("📤 URL: ${ApiClient.retrofit.baseUrl()}auth/login")

    val call = apiService.login(request)

    call.enqueue(object : Callback<LoginResponse> {
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            // 🔥 DEBUG: Ver qué responde el servidor
            println("📥 Respuesta del servidor: ${response.code()} - ${response.message()}")
            println("📥 Body: ${response.body()?.toString()}")
            println("📥 ErrorBody: ${response.errorBody()?.string()}")

            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    onResult(true, loginResponse.perfil, null)
                } else {
                    onResult(false, null, "Respuesta vacía del servidor")
                }
            } else {
                onResult(false, null, "Credenciales incorrectas")
            }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            println("❌ Error de conexión: ${t.message}") // <-- DEBUG
            onResult(false, null, "Error de conexión: ${t.message}")
        }
    })
}
