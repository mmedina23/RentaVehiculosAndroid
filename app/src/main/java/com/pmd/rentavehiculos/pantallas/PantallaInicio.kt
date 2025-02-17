package com.pmd.rentavehiculos.pantallas

import android.graphics.Color.rgb
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pmd.rentavehiculos.R

/*
El NavController es un componente de Jetpack Navigation que se usa en Jetpack
Compose para gestionar la navegación entre pantallas de una aplicación Android.
*/

@Composable
fun PantallaInicio(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo con desenfoque
        Image(
            painter = painterResource(id = R.drawable.logo4), // Asegúrate de que la imagen está en res/drawable
            contentDescription = "Fondo de la pantalla",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        // Capa semitransparente para mejorar la legibilidad
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)) // Oscurece un poco la imagen
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo7),//logo de la app en el header
                contentDescription = "Logo principal",
                modifier = Modifier
                    .width(400.dp)
                    .height(400.dp)
                    .padding(bottom = 20.dp)
            )

            // Botón para alquilar un vehículo
            BotonAnimado(
                text = "Alquilar un Vehículo",
                onClick = { navController.navigate("vehiculos") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botón para ver vehículos alquilados
            BotonAnimado(
                text = "Mis Vehículos Alquilados",
                onClick = { navController.navigate("vehiculos_rentados") }
            )
        }
    }
}

// Composable para un botón con animación y mejor diseño
@Composable
fun BotonAnimado(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(251, 191, 36),
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewPantallaInicio() {
    PantallaInicio(navController = NavController(LocalContext.current)) // Vista previa de la pantalla de inicio
}


