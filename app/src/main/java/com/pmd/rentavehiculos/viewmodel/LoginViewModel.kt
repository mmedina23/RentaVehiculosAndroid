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

                val response = RetrofitClient.authService.login(LoginRequest(username, password))


                apiKey.value = response.llave
                usuario.value = response.persona
                perfil.value = response.perfil  // ← Aquí obtenemos el perfil del usuario

                Log.d("LoginViewModel", "Usuario autenticado: ${usuario.value}")
                Log.d("LoginViewModel", "Perfil obtenido: ${perfil.value}")

                onResult(true, perfil.value ?: "CLIENTE") // ← Se envía a `LoginScreen`
            } catch (e: HttpException) {
                onResult(false, "Error HTTP ${e.code()}")
            } catch (e: Exception) {
                onResult(false, "Error inesperado: ${e.message}")
            }
        }
    }



}
