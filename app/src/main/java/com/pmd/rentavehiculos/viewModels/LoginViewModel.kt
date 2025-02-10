package com.pmd.rentavehiculos.viewModels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.modelo.LoginResponse
import com.pmd.rentavehiculos.modelo.LoginRequest
import com.pmd.rentavehiculos.repositorio.RentaRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository = RentaRepository()

    // LiveData para observar la respuesta de autenticación
    val authResponseLiveData = MutableLiveData<LoginResponse>()

    // LiveData para manejar posibles errores
    val errorLiveData = MutableLiveData<String>()

    // Función para realizar el login
    fun login(usuario: String, contrasena: String) {
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(nombreUsuario = usuario, contrasena = contrasena)
                val authResponse = repository.login(loginRequest)
                authResponseLiveData.value = authResponse
            } catch (ex: Exception) {
                errorLiveData.value = "Error en el login: ${ex.message}"
            }
        }
    }
}