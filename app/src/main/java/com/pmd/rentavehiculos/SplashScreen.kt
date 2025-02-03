package com.pmd.rentavehiculos


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.pmd.rentavehiculos.navegacion.AppScreens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }

    // Simular una comprobación del estado de autenticación
    LaunchedEffect(Unit) {
        delay(2000) // Simula la duración del splash

        val isUserAuthenticated = checkIfUserIsAuthenticated()  // Aquí revisamos si hay un token guardado

        if (isUserAuthenticated) {
            navController.navigate(AppScreens.MainScreen.ruta) {
                popUpTo(AppScreens.SplashScreen.ruta) { inclusive = true }
            }
        } else {
            navController.navigate(AppScreens.LoginScreen.ruta) {
                popUpTo(AppScreens.SplashScreen.ruta) { inclusive = true }
            }
        }
    }
}

// Simulación de la comprobación de autenticación (puede ser con SharedPreferences o DataStore)
fun checkIfUserIsAuthenticated(): Boolean {
    // Aquí debería ir la lógica para verificar si el usuario está autenticado (ej. verificar si hay un token guardado)
    return false  // Por ahora, lo dejamos en false para que siempre vaya al login
}
