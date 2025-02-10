package com.pmd.rentavehiculos.navegacion


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pmd.rentavehiculos.pantallas.LoginScreen
import com.pmd.rentavehiculos.pantallas.PantallaInicio
import com.pmd.rentavehiculos.pantallas.SplashScreen
import com.pmd.rentavehiculos.viewmodel.LoginViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(navController: NavHostController) {
    val loginViewModel: LoginViewModel = viewModel()

    NavHost(navController, startDestination = "splash") {
        // Ruta para el SplashScreen (Pantalla de inicio)
        composable("splash") {
            SplashScreen(navController)
        }

        // Ruta para la pantalla de Login
        composable("login") {
            LoginScreen(navController, loginViewModel)
        }

        // Ruta para el MenuScreen (pantalla del menú)
        composable("menu") {
            PantallaInicio(navController)
        }
    }
}
