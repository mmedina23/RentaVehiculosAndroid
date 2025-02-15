package com.pmd.rentavehiculos.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.pmd.rentavehiculos.data.SessionManager
import com.pmd.rentavehiculos.viewModels.LoginViewModel
import com.pmd.rentavehiculos.screens.LoginScreen
import com.pmd.rentavehiculos.activity.ClienteActivity
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
                },
                onExit = {
                    finish() // Cierra la actividad, lo que en este caso cierra la aplicación
                }
            )
        }

        // Observa la respuesta exitosa del login
        loginViewModel.authResponseLiveData.observe(this) { authResponse ->
            // Guarda el token recibido
            sessionManager.token = authResponse.token
            // Guarda el ID de la persona
            sessionManager.personaId = authResponse.persona.id
            // Guarda el objeto completo de Persona en la sesión
            sessionManager.persona = authResponse.persona

            Toast.makeText(this, "Login exitoso. Perfil: ${authResponse.perfil}", Toast.LENGTH_SHORT).show()

            // Navega a la pantalla correspondiente según el perfil
            when (authResponse.perfil.lowercase()) {
                "admin" -> {
                    val intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                    finish() // Cierra LoginActivity para que no se quede en la pila
                }
                "cliente" -> {
                    val intent = Intent(this, ClienteActivity::class.java)
                    startActivity(intent)
                    finish() // Cierra LoginActivity para que no se quede en la pila
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