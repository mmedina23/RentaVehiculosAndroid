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

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    isLoading = true
                    login(apiService, username, password) { success, role ->
                        isLoading = false
                        if (success) {
                            val intent = if (role == "admin") {
                                Intent(context, AdminActivity::class.java)
                            } else {
                                Intent(context, ClienteActivity::class.java)
                            }
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
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

fun login(apiService: ApiService, username: String, password: String, onResult: (Boolean, String) -> Unit) {
    val call = apiService.login(LoginRequest(username, password))

    call.enqueue(object : Callback<LoginResponse> {
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    onResult(true, loginResponse.role)
                } else {
                    onResult(false, "")
                }
            } else {
                onResult(false, "")
            }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            onResult(false, "")
        }
    })
}
