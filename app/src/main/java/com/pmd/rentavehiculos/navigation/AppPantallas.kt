package com.pmd.rentavehiculos.navigation

sealed class appPantallas (val ruta :String){
    object splashScreen: appPantallas("splash_screen")
    object LoginScreen: appPantallas("pantalla_login")
    object MenuPrincipal: appPantallas("Pantalla_Principal")
    object PantallaCliente: appPantallas("Vista_Cliente")
    object PantallaAdmin: appPantallas("Vista_Admin")
    object VehiculoScreen: appPantallas("Pantalla-Coches")
    object PersonaScreen: appPantallas("Pantalla-Coches")

}