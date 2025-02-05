package com.pmd.rentavehiculos

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.pmd.rentavehiculos.core.Navigation
import com.pmd.rentavehiculos.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val loginViewModel: LoginViewModel by viewModels()

            Navigation(navController, loginViewModel)
        // Este cambio le hice para evitar que LoginViewModel se reinicie cada vez que Navigation se recompone y así asegurarme de que el usuario logueado se mantiene en toda la app,
            // ya que si no el usuario me aparece como null en algunas pantallas.
        }
    }
}

