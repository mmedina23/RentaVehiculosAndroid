package com.pmd.rentavehiculos.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.dataModel.LoginRequest
import com.pmd.rentavehiculos.retrofit.RetrofitClient
import com.pmd.rentavehiculos.utils.SessionManager
import kotlinx.coroutines.launch

class LoginViewModel(private val sessionManager: SessionManager) : ViewModel() {

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
                    sessionManager.saveApiKey(response.llave) // Guarda la llave
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

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val sessionManager = SessionManager(context)
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
