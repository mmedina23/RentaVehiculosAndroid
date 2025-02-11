package com.pmd.rentavehiculos.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.pmd.rentavehiculos.data.SessionManager
import com.pmd.rentavehiculos.viewModels.LoginViewModel
import com.pmd.rentavehiculos.screens.LoginScreen

class LoginActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa el SessionManager
        sessionManager = SessionManager(this)

        // Configura la UI con Compose
        setContent {
            LoginScreen(
                onLogin = { usuario, contrasena ->
                    loginViewModel.login(usuario, contrasena)
                }
            )
        }

        // Observa la respuesta exitosa del login
        loginViewModel.authResponseLiveData.observe(this) { authResponse ->
            // Guarda el token recibido
            sessionManager.token = authResponse.token

            // Notifica al usuario que el login fue exitoso y muestra el perfil
            Toast.makeText(this, "Login exitoso. Perfil: ${authResponse.perfil}", Toast.LENGTH_SHORT).show()

            // Navega a la pantalla correspondiente según el perfil
            when (authResponse.perfil.lowercase()) {
                "admin" -> {
                    val intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                }
                "cliente" -> {
                    val intent = Intent(this, ClienteActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    Toast.makeText(this, "Perfil desconocido", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Observa los errores en el login
        loginViewModel.errorLiveData.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}