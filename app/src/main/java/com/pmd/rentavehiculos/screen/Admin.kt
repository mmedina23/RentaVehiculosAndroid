package com.pmd.rentavehiculos.screen


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Admin(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(), // Ocupa toda la pantalla
        contentAlignment = Alignment.Center // Centra el contenido
    ) {
        Text(
            text = "ADMIN", // Texto a mostrar
            fontSize = 48.sp, // Tamaño del texto (grande)
            fontWeight = FontWeight.Bold, // Negrita
            color = MaterialTheme.colorScheme.primary, // Color primario del tema
            textAlign = TextAlign.Center // Alineación centrada
        )
    }
}