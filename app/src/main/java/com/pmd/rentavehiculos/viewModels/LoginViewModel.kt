package com.pmd.rentavehiculos.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmd.rentavehiculos.modelo.LoginRequest
import com.pmd.rentavehiculos.modelo.Persona
import com.pmd.rentavehiculos.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * ViewModel encargado de gestionar el proceso de autenticación del usuario.
 * Maneja la solicitud de inicio de sesión y almacena los datos del usuario autenticado.
 */
class LoginViewModel : ViewModel() {

    // Estado para almacenar la API Key obtenida tras un inicio de sesión exitoso.
    var apiKey = mutableStateOf<String?>(null)

    // Estado para almacenar los datos del usuario autenticado.
    var usuario = mutableStateOf<Persona?>(null)

    // Estado para almacenar el perfil del usuario (ADMIN o CLIENTE).
    var perfil = mutableStateOf<String?>(null)

    /**
     * Inicia sesión con las credenciales proporcionadas.
     *
     * @param username Nombre de usuario ingresado.
     * @param password Contraseña ingresada.
     * @param onResult Callback que devuelve `true` si la autenticación es exitosa junto con el perfil del usuario,
     *                 o `false` en caso de error con un mensaje descriptivo.
     */
    fun login(username: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Intentando iniciar sesión con usuario: $username")

                // Enviamos la solicitud de inicio de sesión a la API
                val response = RetrofitClient.loginService.login(LoginRequest(username, password))

                // Guardamos la información obtenida en los estados observables
                apiKey.value = response.llave  // API Key para futuras solicitudes autenticadas.
                usuario.value = response.persona  // Información del usuario autenticado.
                perfil.value = response.perfil  // Perfil del usuario (ejemplo: ADMIN o CLIENTE).

                Log.d("LoginViewModel", "Inicio de sesión exitoso. Perfil: ${perfil.value}")

                // Llamamos al callback indicando éxito en la autenticación y enviamos el perfil.
                onResult(true, perfil.value ?: "CLIENTE")

            } catch (e: HttpException) { // Captura errores HTTP (ejemplo: 401 - No autorizado)
                Log.e("LoginViewModel", "Error HTTP: ${e.code()}", e)
                onResult(false, "Error HTTP ${e.code()}")
            } catch (e: IOException) { // Captura errores de conexión (sin internet o problemas del servidor)
                Log.e("LoginViewModel", "Error de conexión", e)
                onResult(false, "Error de conexión")
            } catch (e: Exception) { // Captura cualquier otro error inesperado
                Log.e("LoginViewModel", "Error inesperado", e)
                onResult(false, "Error inesperado, inténtalo nuevamente")
            }
        }
    }
}
