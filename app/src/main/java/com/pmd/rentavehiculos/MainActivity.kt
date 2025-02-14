package com.pmd.rentavehiculos

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pmd.rentavehiculos.Screens.LoginScreen
import com.pmd.rentavehiculos.Screens.admin
import com.pmd.rentavehiculos.retrofit.RetrofitClient

import com.pmd.rentavehiculos.ui.cliente
import com.pmd.rentavehiculos.ui.listaVehiculos
import com.pmd.rentavehiculos.ui.theme.RentaVehiculosTheme
import com.pmd.rentavehiculos.viewmodels.LoginViewModel
import com.pmd.rentavehiculos.viewmodels.ListaVehiculosViewModel  // ✅ Agregamos el ViewModel correcto
import com.tuempresa.tuapp.ui.splash.SplashScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RentaVehiculosTheme {
                MainApp()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainApp() {
    val navController = rememberNavController()

    // ✅ Crear instancia de LoginViewModel
    val loginViewModel: LoginViewModel = viewModel()

    // ✅ Crear instancia de ListaVehiculosViewModel correctamente
    val listaVehiculosViewModel: ListaVehiculosViewModel = viewModel(
        factory = ListaVehiculosViewModel.Factory(RetrofitClient.apiService)
    )

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController, loginViewModel) }
        composable("admin") { admin(navController) }
        composable("cliente") { cliente(navController) }
        composable("ListaVehiculos") {
            // 🔥 Asegurar que la apiKey no está vacía antes de pasarla
            if (!loginViewModel.apiKey.value.isNullOrEmpty()) {
                listaVehiculosViewModel.setCredentials(
                    loginViewModel.apiKey.value!!,
                    loginViewModel.usuario.value?.id ?: -1
                )
                listaVehiculos(navController, listaVehiculosViewModel)
            } else {
                Log.e("API_DEBUG", "Error: apiKey está vacía antes de iniciar ListaVehiculosViewModel")
            }
        }
    }
}


