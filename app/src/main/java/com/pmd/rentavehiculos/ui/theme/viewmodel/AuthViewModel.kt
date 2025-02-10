package com.pmd.rentavehiculos.ui.theme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.data.RetrofitServiceFactory
import com.pmd.rentavehiculos.data.model.LoginRequest
import com.pmd.rentavehiculos.data.model.LoginResponse
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val retrofitService = RetrofitServiceFactory.RetrofitService()

    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult: LiveData<LoginResponse?> get() = _loginResult

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun login(nombreUsuario: String, contrasena: String) {
        viewModelScope.launch {
            try {
                // Llamar al método login de RetrofitService
                val response = retrofitService.login(LoginRequest(nombreUsuario, contrasena))
                _loginResult.postValue(response) // Publicar el resultado en LiveData
            } catch (e: Exception) {
                _errorMessage.postValue("Error: ${e.message}")
            }
        }
    }
}
