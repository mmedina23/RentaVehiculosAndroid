package com.pmd.rentavehiculos.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pmd.rentavehiculos.pantallas.LoginScreen
import com.pmd.rentavehiculos.pantallas.MenuAdminScreen
import com.pmd.rentavehiculos.pantallas.MenuScreen
import com.pmd.rentavehiculos.pantallas.SplashScreen
import com.pmd.rentavehiculos.pantallas.VehiculosAdminScreen
import com.pmd.rentavehiculos.pantallas.VehiculosRentadosAdminScreen
import com.pmd.rentavehiculos.pantallas.VehiculosRentadosScreen
import com.pmd.rentavehiculos.pantallas.VehiculosScreen
import com.pmd.rentavehiculos.viewModels.LoginViewModel
import com.pmd.rentavehiculos.viewModels.VehiculosViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(navController: NavHostController, loginViewModel: LoginViewModel) { // ✅ Recibe el ViewModel desde MainActivity
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
                MenuScreen(navController, loginViewModel) // ✅ Ahora recibe el mismo LoginViewModel
            }
        }
        composable("menu_admin") {
            MenuAdminScreen(navController)
        }
        composable("menu_cliente") {
            MenuScreen(navController, loginViewModel) // ✅ Se lo pasamos aquí también
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
            val vehiculoId = backStackEntry.arguments?.getString("vehiculoId")?.toIntOrNull() ?: 0
            VehiculosRentadosAdminScreen(navController, vehiculoId, vehiculosViewModel, loginViewModel)
        }
    }
}
