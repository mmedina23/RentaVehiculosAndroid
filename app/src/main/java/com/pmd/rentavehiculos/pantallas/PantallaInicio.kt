package com.pmd.rentavehiculos.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pmd.rentavehiculos.R

@Composable
fun PantallaInicio(navController: NavController) {
    // Usamos una Column para apilar los elementos verticalmente con un fondo negro
    Column(
        modifier = Modifier
            .fillMaxSize() // Rellena todo el tamaño de la pantalla
            .background(Color(0, 0, 0)) // Fondo negro para la pantalla (RGB(0, 0, 0))
            .padding(20.dp), // Agrega un padding general a toda la columna
        horizontalAlignment = Alignment.CenterHorizontally // Centra los elementos horizontalmente
    ) {
        // Imagen del logo en la parte superior
        Image(
            painter = painterResource(id = R.drawable.logo5), // Carga el recurso de imagen
            contentDescription = "Logo principal", // Descripción accesible para la imagen
            modifier = Modifier
                .width(250.dp) // Establece un ancho fijo para la imagen
                .height(300.dp) // Establece una altura fija para la imagen
                .padding(bottom = 20.dp) // Espacio entre la imagen y el texto
        )

        // Título en la parte superior de la pantalla
        Text(
            text = "Bienvenidos a Nuestro Servicio", // Título informativo
            fontSize = 32.sp, // Tamaño de fuente más grande
            fontWeight = FontWeight.Bold, // Peso de la fuente en negrita
            fontFamily = FontFamily.Serif, // Tipo de fuente Serif para un estilo clásico
            color = Color(255, 111, 0), // Color naranja para el texto (RGB(255, 111, 0))
            modifier = Modifier.padding(bottom = 20.dp) // Espacio entre el texto y los botones
        )

        // Primer botón para alquilar un vehículo
        Button(
            onClick = { navController.navigate("vehiculos") }, // Navega a la pantalla "vehiculos"
            modifier = Modifier.fillMaxWidth(0.75f), // Ocupa el 75% del ancho disponible
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(255, 111, 0), // Color de fondo naranja para el botón (RGB(255, 111, 0))
                contentColor = Color(0, 0, 0) // Texto negro en el botón (RGB(0, 0, 0))
            ),
            shape = RoundedCornerShape(12.dp) // Bordes del botón redondeados
        ) {
            Text("Alquilar un Vehículo", fontSize = 16.sp) // Texto dentro del botón
        }

        Spacer(modifier = Modifier.height(16.dp)) // Espacio entre los botones

        // Segundo botón para ver los vehículos alquilados
        Button(
            onClick = { navController.navigate("vehiculos_rentados") }, // Navega a la pantalla "vehiculos_rentados"
            modifier = Modifier.fillMaxWidth(0.75f), // Ocupa el 75% del ancho disponible
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(255, 111, 0), // Mismo color de fondo naranja (RGB(255, 111, 0))
                contentColor = Color(0, 0, 0) // Texto negro en el botón (RGB(0, 0, 0))
            ),
            shape = RoundedCornerShape(12.dp) // Bordes redondeados del botón
        ) {
            Text("Mis Vehículos Alquilados", fontSize = 16.sp) // Texto dentro del botón
        }
    }
}


