package com.pmd.rentavehiculos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.pmd.rentavehiculos.navigation.AppNavigation
import com.pmd.rentavehiculos.ui.theme.RentaVehiculosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RentaVehiculosTheme {
                // Configuración del NavController
                val navController = rememberNavController()

                // Scaffold para manejar la estructura
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding: PaddingValues ->
                    // Ajustar el padding del contenido
                    AppNavigation(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
