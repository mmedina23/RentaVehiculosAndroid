package com.pmd.rentavehiculos.core


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pmd.rentavehiculos.Screen.LoginScreen
import com.pmd.rentavehiculos.Screen.MenuAdminScreen
import com.pmd.rentavehiculos.Screen.MenuScreen
import com.pmd.rentavehiculos.Screen.SplashScreen
import com.pmd.rentavehiculos.Screen.VehiculosAdminScreen
import com.pmd.rentavehiculos.Screen.VehiculosRentadosAdminScreen
import com.pmd.rentavehiculos.Screen.VehiculosRentadosScreen
import com.pmd.rentavehiculos.Screen.VehiculosScreen
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

        composable("vehiculos_disponibles") {
            VehiculosAdminScreen(navController, vehiculosViewModel, loginViewModel)
        }

        composable("vehiculos_rentados_admin/{vehiculoId}") { backStackEntry ->
            val vehiculoId = backStackEntry.arguments?.getString("vehiculoId")?.toIntOrNull() ?: 0
            VehiculosRentadosAdminScreen(navController, vehiculoId, vehiculosViewModel, loginViewModel)
        }


        composable("vehiculos_rentados") {
            val context = LocalContext.current  // ✅ Obtener el contexto de la app
            VehiculosRentadosScreen(navController, vehiculosViewModel, loginViewModel, context)
        }


    }
}
