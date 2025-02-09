package com.pmd.rentavehiculos.screen

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.pmd.rentavehiculos.databinding.ActivityLoginBinding
import com.pmd.rentavehiculos.viewmodel.LoginViewModel
import com.pmd.rentavehiculos.viewmodel.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels { LoginViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.login(email, password)
            } else {
                Toast.makeText(this, "Por favor, completa los campos", Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.usuario.observe(this) { usuario ->
            if (usuario != null) {
                Toast.makeText(this, "Bienvenido, ${usuario.persona.nombre}", Toast.LENGTH_SHORT).show()
                when (usuario.perfil) {
                    "ADMIN" -> startActivity(Intent(this, AdminActivity::class.java))
                    "CLIENTE" -> startActivity(Intent(this, ClienteActivity::class.java))
                }
                finish()
            }
        }

        loginViewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
