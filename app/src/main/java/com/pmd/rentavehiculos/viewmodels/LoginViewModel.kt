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
    var token = mutableStateOf<String?>(null)
    var perfil = mutableStateOf<String?>(null)
    var personaId = mutableStateOf<Int?>(null) // ✅ Guardamos personaId

    fun login(username: String, password: String, onResult: (Boolean, String?, String?, Int?) -> Unit) {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)
        val call = apiService.login(LoginRequest(username, password))

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        perfil.value = loginResponse.perfil
                        token.value = loginResponse.llave
                        personaId.value = loginResponse.personaId // ✅ Obtenemos `personaId`
                        onResult(true, loginResponse.perfil, loginResponse.llave, loginResponse.personaId)
                    } else {
                        onResult(false, null, null, null)
                    }
                } else {
                    onResult(false, null, null, null)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onResult(false, null, null, null)
            }
        })
    }
}
