package com.pmd.rentavehiculos.navegacion

sealed class AppScreens(val ruta :String){
    object SplashScreen : AppScreens("splash_screen")
    object MainList : AppScreens("main_list")
    object MenuScreen : AppScreens("menu_screen")
    object MostrarNave : AppScreens("mostrar_nave")
    object AddNave : AppScreens("add_nave")
}