package com.pmd.rentavehiculos.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.data.RetrofitServiceFactory
import com.pmd.rentavehiculos.data.model.LoginRequestDto
import com.pmd.rentavehiculos.data.model.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class AutenticacionViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val apiService = RetrofitServiceFactory.authService()

    fun login(username: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            try {
                val response = apiService.login(LoginRequestDto(username, password))
                if (response.isSuccessful) {
                    response.body()?.let {
                        _loginState.value = LoginState.Success(it)
                    } ?: run {
                        _loginState.value = LoginState.Error("Respuesta vacía del servidor.")
                    }
                } else {
                    _loginState.value = LoginState.Error("Error ${response.code()}: ${response.message()}")
                }
            } catch (e: IOException) {
                _loginState.value = LoginState.Error("Error de red: ${e.localizedMessage}")
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Error inesperado: ${e.localizedMessage}")
            }
        }
    }
}
