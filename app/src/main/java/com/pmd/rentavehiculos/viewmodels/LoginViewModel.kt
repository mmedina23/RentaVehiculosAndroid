package com.pmd.rentavehiculos.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pmd.rentavehiculos.models.LoginRequest
import com.pmd.rentavehiculos.models.LoginResponse
import com.pmd.rentavehiculos.network.ApiClient
import com.pmd.rentavehiculos.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    var perfil = mutableStateOf("")

    fun login(username: String, password: String, onResult: (Boolean, String) -> Unit) {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)
        val call = apiService.login(LoginRequest(username, password))

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        perfil.value = loginResponse.perfil
                        onResult(true, loginResponse.perfil)
                    } else {
                        onResult(false, "")
                    }
                } else {
                    onResult(false, "")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onResult(false, "")
            }
        })
    }
}
