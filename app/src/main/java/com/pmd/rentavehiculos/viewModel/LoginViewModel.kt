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
    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult: LiveData<LoginResponse?> get() = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.login(LoginRequest(username, password))
                if (response.isSuccessful) {
                    _loginResult.postValue(response.body())
                }
            } catch (e: Exception) {
                // Manejar errores
            }
        }
    }
}
