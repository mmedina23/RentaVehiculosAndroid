package com.pmd.rentavehiculos.ui.theme.home

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.pmd.rentavehiculos.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(1300) // ⏳ Espera 3 segundos antes de navegar
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true } // 🔹 Evita que el usuario vuelva atrás
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxSize()
            ) {
            Image(
                painter = painterResource(id = R.drawable.splash_screen_innovador),
                contentDescription = "Splash Screen",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // 📌 Recorta y llena la pantalla sin distorsión
            )
        }
    }
}
