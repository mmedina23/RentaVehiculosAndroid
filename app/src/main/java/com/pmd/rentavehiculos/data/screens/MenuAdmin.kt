package com.pmd.rentavehiculos.data.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class MenuAdmin : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController() // Fix: Define a proper NavController
            opcionesAdmin(navController)
        }
    }
}

@Composable
fun opcionesAdmin(navController: NavHostController){
    Text(
        text = "Menu Admin"

    )

    Column {
        Text(text = "Opciones")
        Button(onClick = ()) {
            Text(text = "Ver Vehiculos Disponibles")
        }
        Button(onClick = ()) {
            Text(text = "Ver Vehiculos Rentados")
        }

    }

}