package com.pmd.rentavehiculos.views


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.models.LoginRequest
import com.pmd.rentavehiculos.models.LoginResponse
import com.pmd.rentavehiculos.network.ApiClient
import com.pmd.rentavehiculos.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        apiService = ApiClient.retrofit.create(ApiService::class.java)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                login(username, password)
            } else {
                Toast.makeText(this, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(username: String, password: String) {
        val call = apiService.login(LoginRequest(username, password))

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        Toast.makeText(applicationContext, "Bienvenido ${loginResponse.role}", Toast.LENGTH_SHORT).show()

                        // Redirigir según el rol
                        val intent = if (loginResponse.role == "admin") {
                            Intent(applicationContext, AdminActivity::class.java)
                        } else {
                            Intent(applicationContext, ClienteActivity::class.java)
                        }
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Toast.makeText(applicationContext, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
