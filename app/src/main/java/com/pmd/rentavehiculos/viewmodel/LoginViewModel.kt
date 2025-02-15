package com.pmd.rentavehiculos.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.model.LoginRequest
import com.pmd.rentavehiculos.model.Persona
import com.pmd.rentavehiculos.remote.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel : ViewModel() {
    var apiKey = mutableStateOf<String?>(null)
    var usuario = mutableStateOf<Persona?>(null)
    var perfil = mutableStateOf<String?>(null)
    fun login(username: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "🔐 Iniciando sesión para: $username")

                val response = RetrofitClient.authService.login(LoginRequest(username, password))

                // Guarda la llave API obtenida
                apiKey.value = response.llave
                usuario.value = response.persona
                perfil.value = response.perfil

                Log.d("LoginViewModel", "✅ Sesión iniciada, API Key: ${apiKey.value}")

                onResult(true, perfil.value ?: "CLIENTE")
            } catch (e: HttpException) {
                Log.e("LoginViewModel", "❌ Error HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}")
                onResult(false, "Error HTTP ${e.code()}")
            } catch (e: IOException) {
                Log.e("LoginViewModel", "❌ Error de conexión")
                onResult(false, "Error de conexión")
            } catch (e: Exception) {
                Log.e("LoginViewModel", "❌ Error inesperado: ${e.message}")
                onResult(false, "Error inesperado")
            }
        }
    }

}