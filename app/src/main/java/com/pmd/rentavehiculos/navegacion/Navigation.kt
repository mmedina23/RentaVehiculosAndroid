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

        // ✅ Modificación: Ahora SplashScreen recibe `navController` correctamente
        composable("splash") {
            SplashScreen(navController) // ✅ Pasamos `navController` directamente
        }

        // ✅ Pantalla de Login
        composable("login") {
            LoginScreen(navController, loginViewModel)
        }

        // ✅ Menú ADMIN recibe token
        composable("menu_admin/{token}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            MenuAdminScreen(navController, token)
        }

        // ✅ Menú CLIENTE recibe token
        composable("menu_cliente/{token}/{personaId}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val personaId = backStackEntry.arguments?.getString("personaId")?.toIntOrNull() ?: 0
            ClienteMenuScreen(navController, token, personaId) // ✅ Pasamos `personaId`
        }


        // ✅ Vehículos Disponibles (Recibe token correctamente)
        composable("vehiculos/{token}/{personaId}") { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token") ?: ""
            val personaId = backStackEntry.arguments?.getString("personaId")?.toIntOrNull() ?: 0
            VehiculosScreen(token, personaId) // ✅ Ahora pasamos personaId correctamente
        }

        }

    }