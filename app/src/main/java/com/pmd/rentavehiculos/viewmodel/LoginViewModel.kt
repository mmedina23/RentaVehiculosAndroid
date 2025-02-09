package com.pmd.rentavehiculos.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pmd.rentavehiculos.model.AuthResponse
import com.pmd.rentavehiculos.model.Persona
import com.pmd.rentavehiculos.remote.ApiClient
import com.pmd.rentavehiculos.remote.ApiService
import com.pmd.rentavehiculos.remote.LoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = ApiClient.createService(ApiService::class.java)
    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _usuario = MutableLiveData<AuthResponse?>()
    val usuario: LiveData<AuthResponse?> get() = _usuario

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun login(nombreUsuario: String, contrasena: String) {
        val request = LoginRequest(nombre_usuario = nombreUsuario, contrasena = contrasena)

        apiService.login(request).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    authResponse?.let {
                        guardarSesion(it)
                        _usuario.value = it
                    } ?: run {
                        _error.value = "Error en los datos de usuario"
                    }
                } else {
                    _error.value = "Credenciales incorrectas"
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                _error.value = "Error de conexión: ${t.message}"
            }
        })
    }

    private fun guardarSesion(authResponse: AuthResponse) {
        sharedPreferences.edit()
            .putString("llave", authResponse.llave)
            .putString("perfil", authResponse.perfil)
            .putInt("persona_id", authResponse.persona.id)
            .putString("nombre", authResponse.persona.nombre)
            .apply()
    }

    fun cerrarSesion() {
        sharedPreferences.edit().clear().apply()
        _usuario.value = null
    }
}
