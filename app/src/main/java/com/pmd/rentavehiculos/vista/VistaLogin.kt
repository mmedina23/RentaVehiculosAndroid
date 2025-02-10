package com.pmd.rentavehiculos.vista

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.model.LoginSolicitud
import com.pmd.rentavehiculos.model.Persona
import com.pmd.rentavehiculos.remote.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class VistaLogin : ViewModel() {
    var apiKey = mutableStateOf<String?>(null)
    var usuario = mutableStateOf<Persona?>(null)
    var perfil = mutableStateOf<String?>(null)


    fun login(username: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "sesion: $username")

                val response = RetrofitClient.authService.login(LoginSolicitud(username, password))
                apiKey.value = response.llave
                usuario.value = response.persona
                perfil.value = response.perfil


                //PRUEBAS POR SI ME FALLA LA API
                Log.d("LoginViewModel", "Usuarioo: ${usuario.value}")
                Log.d("LoginViewModel", "Perfil: ${perfil.value}")

                onResult(true, perfil.value ?: "CLIENTE")  // ESTO ME DEVUELVE EL RETURN CON LA VALIDACION DEL EMNU CLIENTE

            } catch (e: HttpException) {
                onResult(false, "Error HTTP ${e.code()}")
            } catch (e: IOException) {
                onResult(false, "Error de conexión")
            }
        }
    }


}
