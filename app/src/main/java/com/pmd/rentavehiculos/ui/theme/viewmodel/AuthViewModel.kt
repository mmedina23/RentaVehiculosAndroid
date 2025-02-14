package com.pmd.rentavehiculos.ui.theme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.data.model.LoginRequest
import com.pmd.rentavehiculos.data.model.LoginResponse
import com.pmd.rentavehiculos.data.repository.RetrofitClient
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val authService = RetrofitClient.authService

    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult: LiveData<LoginResponse?> get() = _loginResult

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun login(nombreUsuario: String, contrasena: String) {
        viewModelScope.launch {
            try {
                val response = authService.login(LoginRequest(nombreUsuario, contrasena))
                _loginResult.postValue(response)
            } catch (e: Exception) {
                _errorMessage.postValue("Error: ${e.message}")
            }
        }
    }
}
