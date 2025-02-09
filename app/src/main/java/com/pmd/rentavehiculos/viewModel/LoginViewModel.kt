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
                if (response.perfil == "ADMIN" || response.perfil == "CLIENTE") {
                    onSuccess(response.perfil)
                } else {
                    onError("Rol desconocido")
                }
            } catch (e: Exception) {
                onError("Error en el login: ${e.localizedMessage}")
            }
        }
    }
}
