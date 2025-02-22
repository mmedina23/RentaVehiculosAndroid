package com.pmd.rentavehiculos.nav

import MenuClienteScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pmd.rentavehiculos.screens.LogScreen
import com.pmd.rentavehiculos.viewmodels.LoginViewModel
import com.pmd.rentavehiculos.viewmodels.VehiculosViewModel

@Composable
fun Navigation(navController: NavHostController, loginVM: LoginViewModel, vehiculosVM: VehiculosViewModel) {

    NavHost(navController, startDestination = "login") {
        composable("login") {
            LogScreen(navController, loginVM)
        }
        composable("menu_ad") {
            //MenuAdminScreen(navController, loginVM)
        }
        composable("menu_cl") {
            MenuClienteScreen(navController, loginVM, vehiculosVM)
        }
    }
}