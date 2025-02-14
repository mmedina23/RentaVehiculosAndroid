package com.pmd.rentavehiculos.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pmd.rentavehiculos.ui.auth.LoginScreen
import com.pmd.rentavehiculos.ui.theme.admin.AdminHomeScreen
import com.pmd.rentavehiculos.ui.theme.home.HomeScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    context: Context,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(
                navController = navController,
                context = context // Pasamos el contexto
            )
        }
        composable("home") { HomeScreen() }
        composable(
            route = "admin_home/{id}", // Ruta dinámica
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            AdminHomeScreen(
                navController = navController,
                context = context,
                id = id
            )
        }


    }
}
