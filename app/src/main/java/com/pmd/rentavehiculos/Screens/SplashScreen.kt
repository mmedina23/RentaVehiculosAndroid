package com.pmd.rentavehiculos.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pmd.rentavehiculos.Core.Login
import com.pmd.rentavehiculos.Core.Personas
import com.pmd.rentavehiculos.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(NavigationController : NavController){
    Splash()

    LaunchedEffect(key1 = true) {
        delay(3000)
        NavigationController.navigate(Login){
            NavigationController.popBackStack()
        }
    }
}

val SanFrancisco = FontFamily(
    Font(R.font.poppins_extra_light)
)

@Composable
fun Splash(){
    Box(modifier = Modifier
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(Color.White, Color.Black)
            )
        )
        .fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Text(text = "Renta vehiculos", fontSize = 40.sp, color = Color.Black, fontFamily = SanFrancisco);
    }
}

@Preview
@Composable
fun previewSplash(){
    Splash()
}