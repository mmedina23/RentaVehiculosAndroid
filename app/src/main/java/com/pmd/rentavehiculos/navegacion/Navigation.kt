package com.pmd.rentavehiculos.navegacion

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pmd.rentavehiculos.pantallas.LoginScreen
import com.pmd.rentavehiculos.pantallas.PantallaInicio
import com.pmd.rentavehiculos.pantallas.PantallaInicioAdmi
import com.pmd.rentavehiculos.pantallas.SplashScreen
import com.pmd.rentavehiculos.pantallas.VehiculosRentadosScreen
import com.pmd.rentavehiculos.pantallas.VehiculosScreen
import com.pmd.rentavehiculos.viewmodel.LoginViewModel
import com.pmd.rentavehiculos.viewmodel.VehiculosViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(navController: NavHostController) {
    val loginViewModel: LoginViewModel = viewModel()
    val vehiculosViewModel: VehiculosViewModel = viewModel()

    NavHost(navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("login") {
            LoginScreen(navController, loginViewModel)
        }
        composable("menu") {
            val perfil = loginViewModel.perfil.value
            if (perfil == "ADMIN") {
                PantallaInicioAdmi(navController)
            } else {
                PantallaInicio(navController)
            }
        }
        composable("menu_admin") {
            PantallaInicioAdmi(navController)
        }
        composable("menu_cliente") {
            PantallaInicio(navController)
        }
        composable("vehiculos") {
            VehiculosScreen(navController, vehiculosViewModel, loginViewModel)
        }
        composable("vehiculos_rentados") {
            VehiculosRentadosScreen(navController, vehiculosViewModel, loginViewModel)
        }

    }
}
