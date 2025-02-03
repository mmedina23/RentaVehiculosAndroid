package com.pmd.rentavehiculos.navegacion

sealed class AppScreens(val ruta: String) {
    object SplashScreen : AppScreens("splash_screen")
    object LoginScreen : AppScreens("login_screen")   // Nueva pantalla de login
    object MainScreen : AppScreens("main_screen")
}
