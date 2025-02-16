package com.pmd.rentavehiculos.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.modelo.Vehiculo
import com.pmd.rentavehiculos.viewModels.VehiculosViewModel
@Composable
fun VehiculosDisponiblesScreen(
    apiKey: String,
    onVehiculoClick: (Vehiculo) -> Unit,
    onBackClick: () -> Unit, // Acción para volver atrás
    vehiculosViewModel: VehiculosViewModel = viewModel()
) {
    // Llama a la función para obtener vehículos disponibles al iniciar
    LaunchedEffect(Unit) {
        vehiculosViewModel.obtenerVehiculosDisponibles(apiKey)
    }
    val vehiculos by vehiculosViewModel.vehiculosDisponiblesLiveData.observeAsState(emptyList())
    val errorMessage by vehiculosViewModel.errorLiveData.observeAsState()

    // Uso un Box para colocar el fondo y el contenido encima
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.disponiblecliente),
            contentDescription = "Fondo del menú de cliente",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 180.dp)
                .padding(horizontal = 32.dp, vertical = 48.dp)
                .background(Color.White.copy(alpha = 0.90f), shape = RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(55.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver atrás",
                        modifier = Modifier.size(50.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp)
                ) {
                    Text(
                        text = "Vehículos",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 40.sp),
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Disponibles",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 40.sp),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFFC107),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Contenido de la pantalla
            when {
                errorMessage != null -> {
                    Text(text = errorMessage!!)
                }
                vehiculos.isEmpty() -> {
                    // Mientras se carga, muestra un indicador
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                else -> {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(vehiculos) { vehiculo ->
                            VehiculoItem(vehiculo = vehiculo) {
                                onVehiculoClick(vehiculo)
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun VehiculoItem(vehiculo: Vehiculo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                Log.d("VehiculoItem", "Se hizo click en el vehículo con id: ${vehiculo.id}")
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.90f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Imagen del vehículo a un lateral
            AsyncImage(
                model = vehiculo.imagen,
                contentDescription = "Imagen de ${vehiculo.marca}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Información del vehículo en una columna
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = vehiculo.marca,
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.Black,
                        fontWeight = FontWeight.Bold)
                )
                Text(
                    text = vehiculo.carroceria,
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
                )
                Text(
                    text = "Plazas: ${vehiculo.plazas}",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
                )
                Text(
                    text = "${vehiculo.valor_dia} al día.",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = Color(0xFFFFC107),
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }
        }
    }
}
