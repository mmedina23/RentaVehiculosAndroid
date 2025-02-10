package com.pmd.rentavehiculos.Pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

import com.pmd.rentavehiculos.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: androidx.navigation.NavController) {
    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo3),
                contentDescription = "Logo de la aplicación",
                modifier = Modifier.size(500.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            /*Text(
                text = "Bienvenido ",
                color = Color(rgb(249, 115, 22)), // Naranja
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )*/
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    val navController = rememberNavController()
    SplashScreen(navController)
}
*/