package com.pmd.rentavehiculos.views

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.navegacion.Navigation
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController() // ✅ Controlador de navegación
            Navigation(navController) // ✅ Llama a la navegación global
        }
    }
}

@Composable
fun SplashScreen(navController: androidx.navigation.NavHostController) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
        delay(3000) // ⏳ Espera 3 segundos
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true } // ✅ Elimina el Splash de la pila de navegación
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(1000)) + scaleIn(),
            exit = fadeOut()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.logo), // 📌 Asegúrate de tener el logo en res/drawable
                    contentDescription = "Logo de la App",
                    modifier = Modifier.size(150.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Renta de Vehículos",
                    fontSize = 24.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}
