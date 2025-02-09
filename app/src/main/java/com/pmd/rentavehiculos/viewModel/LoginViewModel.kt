package com.pmd.rentavehiculos.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.dataModel.LoginRequest
import com.pmd.rentavehiculos.dataModel.LoginResponse
import com.pmd.rentavehiculos.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    fun login(
        username: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.login(
                    LoginRequest(username, password)
                )
                // Verifica si la respuesta es válida
                if (response.rol == "ADMIN" || response.rol == "CLIENTE") {
                    onSuccess(response.rol) // Llama a onSuccess con el perfil del usuario
                } else {
                    onError("Rol desconocido") // Muestra un error si el rol no es válido
                }
            } catch (e: Exception) {
                onError("Error en el login: ${e.localizedMessage}") // Manejo de errores
            }
        }
    }
}
