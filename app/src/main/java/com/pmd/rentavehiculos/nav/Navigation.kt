package com.pmd.rentavehiculos.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pmd.rentavehiculos.screens.LogScreen
import com.pmd.rentavehiculos.viewmodels.LoginViewModel

@Composable
fun Navigation(navController: NavHostController, loginVM: LoginViewModel) {

    NavHost(navController, startDestination = "login") {
        composable("login") {
            LogScreen(navController, loginVM)
        }
        composable("menu") {

        }
    }
}