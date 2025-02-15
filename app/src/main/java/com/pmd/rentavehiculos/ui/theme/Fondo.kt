package com.pmd.rentavehiculos.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.Modifier

@Composable
fun Fondo() {
    val colors = listOf(Color(0xFFFAFAFA), Color(0xFFE0E0E0), Color(0xFFBDBDBD))  // Colores grises claros
    val brush = Brush.linearGradient(colors)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path = Path().apply {
                moveTo(0f, size.height * 0.6f)
                quadraticBezierTo(size.width * 0.5f, size.height * 0.4f, size.width, size.height * 0.6f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }

            drawPath(
                path,
                Brush.linearGradient(
                    listOf(Color(0xFFEEEEEE), Color(0xFFB0BEC5), Color(0xFF90A4AE))  // Tonos grises suaves
                )
            )
        }
    }
}
