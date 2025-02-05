package com.pmd.rentavehiculos.Viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.Data.RetrofitServiceFactory
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    private val service = RetrofitServiceFactory.retrofitService()

    fun login(usuario: String, contrasena: String) {
        // Enviamos las credenciales al servidor
        val credentials = LoginCredentials(usuario, contrasena)

        viewModelScope.launch {
            try {
                // Hacemos la llamada a la API para autenticar al usuario
                val response = service.login(credentials)  // Aquí hacemos la llamada con las credenciales

                // Si la respuesta es válida, actualizamos el estado
                if (response.token != null) {
                    _loginState.value = LoginState(success = true, token = response.token, role = response.role)
                } else {
                    _loginState.value = LoginState(error = "Credenciales incorrectas")
                }
            } catch (e: Exception) {
                // Si ocurre un error de conexión, mostramos un mensaje de error
                _loginState.value = LoginState(error = "Error de conexión: ${e.message}")
            }
        }
    }
}

data class LoginState(
    val success: Boolean = false,
    val error: String? = null,
    val token: String? = null,
    val role: String? = null
)
