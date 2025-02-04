package com.pmd.rentavehiculos.ui.theme.navegacion


sealed class AppScreens(val ruta: String) {
    object SplashScreen : AppScreens("splash_screen")
    object MainScreen : AppScreens("main_screen")
}