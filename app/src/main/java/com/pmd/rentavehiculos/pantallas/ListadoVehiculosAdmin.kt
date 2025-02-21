package com.pmd.rentavehiculos.pantallas


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.ClasesPrincipales.Vehiculo
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.viewmodel.LoginViewModel
import com.pmd.rentavehiculos.viewmodel.VehiculosViewModel


@Composable
fun ListadoVehiculosAdmin(
    navController: NavController,
    vehiculosViewModel: VehiculosViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {
    val apiKey = loginViewModel.apiKey.value
    val vehiculosDisponibles = vehiculosViewModel.vehiculosDisponibles

    LaunchedEffect(apiKey) {
        if (apiKey != null) {
            vehiculosViewModel.obtenerVehiculosDisponibles(apiKey)
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.logo4), // Asegúrate de tener logo4 en res/drawable
            contentDescription = "Fondo de pantalla",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Almacén de Vehículos",
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = Color.White, // Color blanco para contraste
                modifier = Modifier
                    .fillMaxWidth() // Asegura que el texto se alinee al centro
                    .padding(bottom = 16.dp)
            )

            if (vehiculosDisponibles.isEmpty()) {
                Text(
                    "No hay vehículos disponibles en este momento.",
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            } else {
                LazyColumn {
                    items(vehiculosDisponibles) { vehiculo ->
                        VehiculoAdminCard(
                            vehiculo,
                            onEdit = { /* para editar */ },
                            onDelete = { /* Acción de eliminar */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VehiculoAdminCard(vehiculo: Vehiculo, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f)), // Oscuro y semi-transparente
        shape = RoundedCornerShape(12.dp)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Centrar todo
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Imagen del vehículo desde la API
                AsyncImage(
                    model = vehiculo.imagen, // URL de imagen
                    contentDescription = "Imagen del vehículo",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp)), // Bordes redondeados
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${vehiculo.marca}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(text = "Color: ${vehiculo.color}", fontSize = 12.sp, color = Color.White)
                    Text(text = "Carrocería: ${vehiculo.carroceria}", fontSize = 12.sp, color = Color.White)
                    Text(text = "Plazas: ${vehiculo.plazas}", fontSize = 12.sp, color = Color.White)
                    Text(text = "Cambio: ${vehiculo.cambios}", fontSize = 12.sp, color = Color.White)
                    Text(text = "Combustible: ${vehiculo.tipo_combustible}", fontSize = 12.sp, color = Color.White)

                    Spacer(modifier = Modifier.weight(1f)) // Empuja "Coste por día" hacia abajo
                }
            }

            // Coste por día centrado y abajo
            Text(
                text = "Coste por día: € ${vehiculo.valor_dia}",
                fontWeight = FontWeight.Bold,
                color = Color(251, 191, 36),
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp), // Espaciado superior
                textAlign = TextAlign.Center // Centrar el texto
            )

            // Espacio entre el precio y los botones
            Spacer(modifier = Modifier.height(12.dp))

            // Botones de Editar y Eliminar centrados
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly // Distribución uniforme
            ) {
                TextButton(
                    onClick = onEdit,
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color(251, 191, 36), // Fondo amarillo dorado
                        contentColor = Color.Black // Texto negro
                    ),
                    modifier = Modifier
                        .background(Color(251, 191, 36), shape = RoundedCornerShape(8.dp)) // Bordes redondeados
                ) {
                    Text(
                        "Editar",
                        fontWeight = FontWeight.Bold
                    )
                }

                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color.Red, // Fondo rojo
                        contentColor = Color.White // Texto blanco
                    ),
                    modifier = Modifier
                        .background(Color.Red, shape = RoundedCornerShape(8.dp)) // Bordes redondeados
                ) {
                    Text(
                        "Eliminar",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
