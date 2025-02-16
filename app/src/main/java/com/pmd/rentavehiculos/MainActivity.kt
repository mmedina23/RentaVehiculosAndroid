//gradlew --stop
package com.pmd.rentavehiculos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmd.rentavehiculos.ui.AdminScreen
import com.pmd.rentavehiculos.ui.ClienteScreen
import com.pmd.rentavehiculos.ui.LoginScreen
import com.pmd.rentavehiculos.ui.theme.RentaVehiculosTheme
import com.pmd.rentavehiculos.viewmodel.LoginState
import com.pmd.rentavehiculos.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RentaVehiculosTheme {
                val viewModel: LoginViewModel = viewModel()
                val loginState by viewModel.loginState.collectAsState()

                when (loginState) {
                    is LoginState.Success -> {
                        val successState = loginState as LoginState.Success
                        val idUsuario = successState.idUsuario
                        val llaveApi = successState.llave
                        val nombreUsuario = successState.mensaje.replace("Bienvenido, ", "")

                        when (successState.perfil) {
                            "ADMIN" -> {
                                AdminScreen(
                                    nombreUsuario = nombreUsuario,
                                    llaveApi = llaveApi,
                                    onLogout = {
                                        viewModel.cerrarSesion(
                                            idUsuario,
                                            llaveApi,
                                            onLogoutSuccess = {
                                                println("Sesión cerrada, regresando a LoginScreen...")
                                            },
                                            onLogoutError = { error ->
                                                println("Error al cerrar sesión: $error")
                                            }
                                        )
                                    }

                                )
                            }

                            "CLIENTE" -> {
                                ClienteScreen(
                                    nombreUsuario = nombreUsuario,
                                    llaveApi = llaveApi,
                                    idUsuario = idUsuario,
                                    onLogout = {
                                        viewModel.cerrarSesion(
                                            idUsuario,
                                            llaveApi,
                                            onLogoutSuccess = {
                                                println("Sesión cerrada, regresando a LoginScreen...")
                                            },
                                            onLogoutError = { error ->
                                                println("Error al cerrar sesión: $error")
                                            }
                                        )
                                    }

                                )
                            }
                        }
                    }

                    else -> {
                        LoginScreen()
                    }
                }
            }
        }
    }
}
