package com.pmd.rentavehiculos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.network.LoginRequest
import com.pmd.rentavehiculos.network.LoginResponse
import com.pmd.rentavehiculos.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    object Loading : LoginState()
    data class Success(val mensaje: String, val perfil: String, val llave: String) : LoginState()
    data class Error(val error: String) : LoginState()
}

class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Loading)
    val loginState: StateFlow<LoginState> = _loginState

    fun iniciarSesion(nombreUsuario: String, contrasena: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                // Hacer la solicitud al login
                val response = RetrofitClient.authApi.login(
                    LoginRequest(nombre_usuario = nombreUsuario, contrasena = contrasena)
                )
                // Si se recibe correctamente, actualiza el estado
                _loginState.value = LoginState.Success(
                    mensaje = "Bienvenido, ${response.persona.nombre}!",
                    perfil = response.perfil,
                    llave = response.llave
                )
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Error: ${e.message}")
            }
        }
    }
}
