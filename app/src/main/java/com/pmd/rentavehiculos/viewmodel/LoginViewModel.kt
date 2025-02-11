package com.pmd.rentavehiculos.viewmodel

import retrofit2.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.data.model.LoginRequest
import com.pmd.rentavehiculos.data.service.RetrofitClient
import com.pmd.rentavehiculos.data.model.Persona
import kotlinx.coroutines.launch
import java.io.IOException

class LoginViewModel : ViewModel() {
    var apiKey = mutableStateOf<String?>(null)
    var usuario = mutableStateOf<Persona?>(null)
    var perfil = mutableStateOf<String?>(null)

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun login(username: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "sesion: $username")

                val response = RetrofitClient.authService.login(LoginRequest(username, password))
                apiKey.value = response.llave
                usuario.value = response.persona
                perfil.value = response.perfil

                // PRUEBAS POR SI ME FALLA LA API
                Log.d("LoginViewModel", "Usuario: ${usuario.value}")
                Log.d("LoginViewModel", "Perfil: ${perfil.value}")

                onResult(true, perfil.value ?: "CLIENTE")
            } catch (e: HttpException) {
                onResult(false, "Error HTTP ${e.code()}")
            } catch (e: IOException) {
                onResult(false, "Error de conexión")
            }
        }
    }
}
