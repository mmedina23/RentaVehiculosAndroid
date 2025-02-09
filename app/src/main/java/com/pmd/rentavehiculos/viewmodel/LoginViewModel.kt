package com.pmd.rentavehiculos.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.model.LoginRequest
import com.pmd.rentavehiculos.model.Persona
import com.pmd.rentavehiculos.logic.RetrofitService
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
                Log.d("LoginViewModel", "➡️ Iniciando sesión con usuario: $username")

                val response = RetrofitService.authService.login(LoginRequest(username, password))

                if (response.llave != null && response.persona != null) {
                    apiKey.value = response.llave
                    usuario.value = response.persona
                    perfil.value = response.perfil ?: "CLIENTE"

                    Log.d("LoginViewModel", "✅ Sesión iniciada correctamente")
                    Log.d("LoginViewModel", "🔑 API Key: ${apiKey.value}")
                    Log.d("LoginViewModel", "👤 Usuario: ${usuario.value}")
                    Log.d("LoginViewModel", "📌 Perfil: ${perfil.value}")

                    onResult(true, perfil.value!!)
                } else {
                    Log.e("LoginViewModel", "⚠️ Error: Respuesta inválida de la API")
                    onResult(false, "Error en la autenticación")
                }

            } catch (e: HttpException) {
                Log.e("LoginViewModel", "❌ Error HTTP ${e.code()} - ${e.message()}")
                onResult(false, "Error HTTP ${e.code()}")
            } catch (e: IOException) {
                Log.e("LoginViewModel", "❌ Error de conexión: ${e.message}")
                onResult(false, "Error de conexión")
            } catch (e: Exception) {
                Log.e("LoginViewModel", "❌ Error desconocido: ${e.localizedMessage}")
                onResult(false, "Error desconocido")
            }
        }
    }
}
