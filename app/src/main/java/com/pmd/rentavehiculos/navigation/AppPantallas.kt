package com.pmd.rentavehiculos.navigation

sealed class appPantallas (val ruta :String){
    object splashScreen: appPantallas("splash_screen")
    object LoginScreen: appPantallas("pantalla_login")
    object MenuScreen: appPantallas("Pantalla_Principal")
    object ClientScreen: appPantallas("Vista_Cliente")
    object AdminScreen: appPantallas("Vista_Admin")
    object VehiculoScreen: appPantallas("Pantalla-Coches")
    object PersonaScreen: appPantallas("Pantalla-Coches")

}