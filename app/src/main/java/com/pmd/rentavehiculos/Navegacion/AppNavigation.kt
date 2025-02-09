package com.pmd.rentavehiculos.Navegacion

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pmd.rentavehiculos. screen.MenuAdminScreen
import com.pmd.rentavehiculos. screen.MenuScreen
import com.pmd.rentavehiculos.screen.Splash
import com.pmd.rentavehiculos.screen.VehiculosRentadosAdminScreen
import com.pmd.rentavehiculos.screen.VehiculosRentadosScreen
import com.pmd.rentavehiculos.screen.VehiculosScreen
import com.pmd.rentavehiculos.screen.VehiculosAdminScreen
import com.pmd.rentavehiculos.screen.LoginScreen
import com.pmd.rentavehiculos.viewmodel.LoginViewModel
import com.pmd.rentavehiculos.viewmodel.VehiculosViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(navController: NavHostController) {
    val loginViewModel: LoginViewModel = viewModel()
    val vehiculosViewModel: VehiculosViewModel = viewModel()

    NavHost(navController, startDestination = "splash") {
        composable("splash") {
            Splash(navController)
        }
        composable("login") {
            LoginScreen(navController, loginViewModel)
        }
        composable("menu") {
            val perfil = loginViewModel.perfil.value
            if (perfil == "ADMIN") {
                MenuAdminScreen(navController)
            } else {
                MenuScreen(navController)
            }
        }
        composable("menu_admin") {
            MenuAdminScreen(navController)
        }
        composable("menu_cliente") {
            MenuScreen(navController)
        }
        composable("vehiculos") {
            VehiculosScreen(navController, vehiculosViewModel, loginViewModel)
        }
        composable("vehiculos_rentados") {
            VehiculosRentadosScreen(navController, vehiculosViewModel, loginViewModel)
        }
        composable("vehiculos_disponibles") {
            VehiculosAdminScreen(navController, vehiculosViewModel, loginViewModel)
        }

        composable("vehiculos_rentados_admin/{vehiculoId}") { backStackEntry ->
            val vehiculoId = backStackEntry.arguments?.getString("vehiculoId")?.toIntOrNull()

            if (vehiculoId != null) {
                VehiculosRentadosAdminScreen(navController, vehiculoId, viewModel(), loginViewModel)
            } else {
                // Redirigir a otra pantalla si el ID no es válido
                navController.navigate("menu_admin")
            }
        }

    }
}




