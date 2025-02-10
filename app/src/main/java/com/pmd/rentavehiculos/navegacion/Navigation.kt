package com.pmd.rentavehiculos.navegacion

package com.pmd.rentavehiculos.navegacion

import SplashScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pmd.rentavehiculos.pantallas.LoginScreen

import com.pmd.rentavehiculos.pantallas.PantallaInicio


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

        //ESTA FUNCION NO LA ENTIENDO MUY BIEN. VOLVER A CHQUEAR
        composable("vehiculos_rentados_admin/{vehiculoId}") { backStackEntry ->
            val vehiculoId = backStackEntry.arguments?.getString("vehiculoId")?.toIntOrNull() ?: 0
            VehiculosRentadosAdminScreen(navController, vehiculoId, viewModel(), loginViewModel)
        }
    }
}
