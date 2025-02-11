package com.pmd.rentavehiculos.vista

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.model.LoginSolicitud
import com.pmd.rentavehiculos.model.Persona
import com.pmd.rentavehiculos.remote.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class VistaLogin : ViewModel() {
    var apiKey = mutableStateOf<String?>(null)
    var usuario = mutableStateOf<Persona?>(null)
    var perfil = mutableStateOf<String?>(null)


    fun login(username: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Iniciando sesión con usuario: $username")

                val response = RetrofitClient.authService.login(LoginSolicitud(username, password))

                Log.d("LoginViewModel", "Respuesta completa: $response")

                apiKey.value = response.llave
                usuario.value = response.persona
                perfil.value = response.perfil

                Log.d("LoginViewModel", "Llave API: ${apiKey.value}")
                Log.d("LoginViewModel", "Usuario: ${usuario.value}")
                Log.d("LoginViewModel", "Perfil: ${perfil.value}")

                onResult(true, perfil.value ?: "CLIENTE")  // Si no hay perfil, asigna "CLIENTE"

            } catch (e: HttpException) {
                Log.e("LoginViewModel", "Error HTTP: ${e.code()} - ${e.message()}")
                onResult(false, "Error HTTP ${e.code()}")
            } catch (e: IOException) {
                Log.e("LoginViewModel", "Error de conexión: ${e.message}")
                onResult(false, "Error de conexión")
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error inesperado: ${e.message}")
                onResult(false, "Error desconocido")
            }
        }
    }



}
