package com.pmd.rentavehiculos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pmd.rentavehiculos.ui.theme.RentaVehiculosTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import com.pmd.rentavehiculos.ui.LoginScreen
import com.pmd.rentavehiculos.ui.VehiculoScreen
import com.pmd.rentavehiculos.viewmodel.LoginState
import com.pmd.rentavehiculos.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: LoginViewModel = viewModel()
            val loginState by viewModel.loginState.collectAsState()

            when (loginState) {
                is LoginState.Success -> {
                    val successState = loginState as LoginState.Success
                    VehiculoScreen(
                        llaveApi = successState.llave,
                        nombreUsuario = successState.mensaje.replace("Bienvenido, ", ""),
                        onLogout = {
                            viewModel.cerrarSesion(
                                idUsuario = successState.idUsuario,
                                llaveApi = successState.llave,
                                onLogoutSuccess = {
                                    viewModel.resetLoginState() // Regresa a LoginScreen
                                },
                                onLogoutError = { errorMsg ->
                                    println("❌ Error al cerrar sesión: $errorMsg")
                                }
                            )
                        }
                    )
                }
                is LoginState.Loading -> {
                    CircularProgressIndicator()
                }
                else -> {
                    LoginScreen { perfil, llave, idUsuarioString ->
                        val idUsuario = idUsuarioString.toIntOrNull() ?: 0 // Convierte a Int o usa 0 si es nulo
                        viewModel.setLoginState(perfil, llave, idUsuario)
                    }

                }
            }
        }
    }
}