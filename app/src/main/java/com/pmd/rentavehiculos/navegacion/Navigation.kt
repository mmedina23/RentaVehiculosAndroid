package com.pmd.rentavehiculos.navegacion

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pmd.rentavehiculos.views.*
import com.pmd.rentavehiculos.viewmodels.LoginViewModel
import com.pmd.rentavehiculos.viewmodels.VehiculoViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(navController: NavHostController) {
    val loginViewModel: LoginViewModel = viewModel()
    val vehiculosViewModel: VehiculoViewModel = viewModel()

    NavHost(navController, startDestination = "splash") {

        // ✅ Modificación: Ahora SplashScreen recibe el `navController`
        composable("splash") {
            SplashScreen(navController) // ✅ Pasamos `navController`
        }

        composable("login") {
            LoginScreen(navController, loginViewModel)
        }

        composable("menu_admin") {
            MenuAdminScreen(navController)
        }

        composable("menu_cliente") {
            ClienteMenuScreen(navController)
        }
    }
}
