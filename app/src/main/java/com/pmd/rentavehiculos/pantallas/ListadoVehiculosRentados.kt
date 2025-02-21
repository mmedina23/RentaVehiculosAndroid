package com.pmd.rentavehiculos.pantallas

import android.graphics.Color.rgb
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import coil.compose.rememberAsyncImagePainter
import com.pmd.rentavehiculos.ClasesPrincipales.RentaRequest
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.viewmodel.LoginViewModel
import com.pmd.rentavehiculos.viewmodel.VehiculosViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListadoVehiculosRentados(
    navController: NavController,
    vehiculosViewModel: VehiculosViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {
    val apiKey = loginViewModel.apiKey.value
    val personaId = loginViewModel.usuario.value?.id
    val rentas = vehiculosViewModel.rentas
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(apiKey, personaId) {
        if (apiKey != null && personaId != null) {
            vehiculosViewModel.obtenerListadoVehiculos(apiKey, personaId)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // Asegura que el fondo sea negro y no deje bordes blancos
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.logo4),
            contentDescription = "Fondo",
            modifier = Modifier.matchParentSize(), // Asegura que cubra todo el espacio sin bordes
            contentScale = ContentScale.Crop // Evita márgenes blancos al expandir la imagen
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)) // Agrega un tono oscuro para mejorar visibilidad
                .padding(16.dp)
        ) {
            if (rentas.isEmpty()) {
                Text(
                    "Sin vehiculos alquilados",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn {
                    items(rentas) { renta ->
                        ListadoRentaCard(renta) { vehiculoId ->
                            if (apiKey != null) {
                                vehiculosViewModel.devolverVehiculo(apiKey, vehiculoId) { success, message ->
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(message)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListadoRentaCard(renta: RentaRequest, onEntregarVehiculo: (Int) -> Unit) {
    val yaEntregado = !renta.fecha_entrega.isNullOrEmpty()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xAA212121) // Gris oscuro semitransparente
        ),
        shape = RoundedCornerShape(16.dp) // Bordes redondeados
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(renta.vehiculo.imagen),
                contentDescription = "Imagen del vehículo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(8.dp) // Espaciado dentro del Card
                    .clip(RoundedCornerShape(12.dp)), // Redondeo para la imagen
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = renta.vehiculo.marca,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Días alquilados: ${renta.dias_renta}", color = Color.LightGray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Total: ${renta.valor_total_renta} €", color = Color.LightGray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Fecha alquiler: ${renta.fecha_renta}", color = Color.LightGray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Fecha entrega estimada: ${renta.fecha_estimada_entrega}", color = Color.LightGray)

            if (yaEntregado) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Vehículo devuelto el: ${renta.fecha_entrega}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(rgb(250, 204, 21))
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))

                // Botón redondeado con degradado
                Button(
                    onClick = { onEntregarVehiculo(renta.vehiculo.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(50), // Botón redondeado
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                colors = listOf(Color(0xFFD32F2F), Color(0xFFB71C1C))
                            ),
                            shape = RoundedCornerShape(50)
                        )
                ) {
                    Text(
                        text = "Entregar Vehículo",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

