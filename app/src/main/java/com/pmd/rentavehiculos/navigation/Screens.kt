package com.pmd.rentavehiculos.navigation


sealed class Screens(val route: String) {
    object Login : Screens("login")
    object AdminHome : Screens("adminHome")
    object ClienteHome : Screens("clienteHome")
}