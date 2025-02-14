package com.pmd.rentavehiculos.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pmd.rentavehiculos.ui.auth.LoginScreen
import com.pmd.rentavehiculos.ui.theme.admin.AdminHomeScreen
import com.pmd.rentavehiculos.ui.theme.cliente.ClienteHomeScreen
import com.pmd.rentavehiculos.ui.theme.home.HomeScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") { LoginScreen(navController) }
        composable("home") { HomeScreen() }
        composable(
            route = "admin_home/{llaveApi}",
            arguments = listOf(navArgument("llaveApi") { type = NavType.StringType })
        ) { backStackEntry ->
            val llaveApi = backStackEntry.arguments?.getString("llaveApi") ?: ""
            AdminHomeScreen(navController = navController, llaveApi = llaveApi)
        }
    }
}

