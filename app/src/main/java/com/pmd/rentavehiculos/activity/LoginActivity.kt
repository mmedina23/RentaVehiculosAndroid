package com.pmd.rentavehiculos.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.data.SessionManager
import com.pmd.rentavehiculos.viewModels.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializa el SessionManager
        sessionManager = SessionManager(this)

        // Obtén las referencias de los elementos de la UI
        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etContrasena = findViewById<EditText>(R.id.etContrasena)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // Observa la respuesta exitosa del login
        loginViewModel.authResponseLiveData.observe(this) { authResponse ->
            // Guarda el token recibido
            sessionManager.token = authResponse.token

            // Notifica al usuario que el login fue exitoso y muestra el perfil
            Toast.makeText(this, "Login exitoso. Perfil: ${authResponse.perfil}", Toast.LENGTH_SHORT).show()

            // Navega a la pantalla correspondiente según el perfil
            // Por ejemplo, si el perfil es "admin", navega a AdminActivity;
            // si es "cliente", navega a ClienteActivity.
            when (authResponse.perfil) {
                "admin" -> {
                    // Navegar a la pantalla de admin
                    val intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                }
                "cliente" -> {
                    // Navegar a la pantalla de cliente
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

        // Configura el botón de login para llamar al ViewModel
        btnLogin.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()

            if (usuario.isNotEmpty() && contrasena.isNotEmpty()) {
                loginViewModel.login(usuario, contrasena)
            } else {
                Toast.makeText(this, "Por favor, ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }
}