package com.pmd.rentavehiculos.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.classes.LogRequest
import com.pmd.rentavehiculos.classes.Persona
import com.pmd.rentavehiculos.remote.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel : ViewModel() {
    var key = mutableStateOf<String?>(null)
    var user = mutableStateOf<Persona?>(null)
    var perfil = mutableStateOf<String?>(null)

    fun login(username: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.authService.login(LogRequest(username, password))
                key.value = response.key
                user.value = response.persona
                perfil.value = response.perfil //ADMIN o CLIENTE


                onResult(true, perfil.value ?: "CLIENTE")

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string() ?: "Unknown error"
                Log.e("LoginViewModel", "HTTP Error ${e.code()}: $errorBody")
                Log.e("LoginViewModel", "usuario: ${username} contraseña: ${password}")
                onResult(false, "Error HTTP ${e.code()}: $errorBody")
            } catch (e: IOException) {
                onResult(false, "Error de conexión")
            }
        }
    }
}