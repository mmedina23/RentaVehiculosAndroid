package com.pmd.rentavehiculos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.network.LoginRequest
import com.pmd.rentavehiculos.network.LogoutRequest
import com.pmd.rentavehiculos.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    data object Loading : LoginState()
    data class Success(
        val mensaje: String,
        val perfil: String,
        val llave: String,
        val idUsuario: Int
    ) : LoginState()
    data class Error(val error: String) : LoginState()
    data object LoggedOut : LoginState() // Representa el estado después del logout
}

class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.LoggedOut)
    val loginState: StateFlow<LoginState> = _loginState

    fun iniciarSesion(nombreUsuario: String, contrasena: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = RetrofitClient.authApi.login(
                    LoginRequest(nombre_usuario = nombreUsuario, contrasena = contrasena)
                )
                _loginState.value = LoginState.Success(
                    mensaje = "Bienvenido, ${response.persona.nombre}!",
                    perfil = response.perfil,
                    llave = response.llave,
                    idUsuario = response.persona.id
                )
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Error: ${e.message}")
            }
        }
    }

    fun cerrarSesion(idUsuario: Int, llaveApi: String, onLogoutSuccess: () -> Unit, onLogoutError: (String) -> Unit)
    {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.authApi.logout(
                    LogoutRequest(id_usuario = idUsuario, llave_api = llaveApi)
                )

                if (response.isSuccessful) {
                    _loginState.value = LoginState.LoggedOut // Cambio aquí en lugar de null
                    onLogoutSuccess()
                    println("Logout exitoso. H2 debería estar actualizada.")
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    onLogoutError("Error en logout: ${response.code()}, $errorBody")
                    println("Error en logout. Código: ${response.code()}, Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                onLogoutError("Excepción en logout: ${e.message}")
            }
        }
    }

}
