package com.pmd.rentavehiculos.Screens.Cliente

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.Api.RetrofitInstance
import com.pmd.rentavehiculos.Entity.Renta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

@Composable
fun ListaMisVehiculos(token: String, personaId: Int) {
    val vehiculosList = remember { mutableStateOf<List<Renta>>(emptyList()) }

    LaunchedEffect(personaId) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val service = RetrofitInstance.makeRetrofitService()
                val response = service.obtenerMisVehiculos(personaId, token)
                withContext(Dispatchers.Main) {
                    vehiculosList.value = response
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ListaMisVehiculos", "Error obteniendo vehículos: ${e.message}")
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(vehiculosList.value) { renta ->
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .animateContentSize(),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    AsyncImage(
                        model = renta.vehiculo.imagen,
                        contentDescription = "Imagen del vehículo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(bottom = 16.dp),
                    )
                    Text(
                        text = "Marca: ${renta.vehiculo.marca}",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                    Text(
                        text = "Color: ${renta.vehiculo.color}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 6.dp),
                    )
                    Text(
                        text = "Carrocería: ${renta.vehiculo.carroceria}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 6.dp),
                    )
                    Text(
                        text = "Plazas: ${renta.vehiculo.plazas}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 6.dp),
                    )
                    Text(
                        text = "Valor del día: ${renta.vehiculo.precioDia} €",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 6.dp),
                    )
                    Text(
                        text = "Disponible: ${renta.vehiculo.disponible}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )
                    Text(
                        text = "Fecha renta: ${renta.fecha_renta}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )
                    Text(
                        text = "Fecha estimada entrega: ${renta.fecha_estimada_entrega}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )

                    val currentDate = System.currentTimeMillis()
                    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    val estimatedDeliveryDate = formatter.parse(renta.fecha_estimada_entrega)?.time ?: 0
                    val daysRemaining = calculateDaysRemaining(currentDate, estimatedDeliveryDate)

                    Text(
                        text = "Días restantes para entrega: $daysRemaining días",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 16.dp),
                    )

                    Button(onClick = {
                        // Usar GlobalScope para lanzar la coroutine
                        GlobalScope.launch {
                            liberarVehiculo(renta.vehiculo.id, token)
                        }
                    }) {
                        Text(text = "Liberar")
                    }

                    /*Button(
                        onClick = {
                            Log.d("ListaMisVehiculos", "Renta seleccionada: ${renta.vehiculo.marca}")
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = "Ver más detalles",
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }*/

                }
            }
        }
    }
}

suspend fun liberarVehiculo(id: Int, token: String) {
    try {
        // Hacer la llamada a la API
        val service = RetrofitInstance.makeRetrofitService()
        val response = service.liberarVehiculo(id, token)

        if (response.isSuccessful) {
            // Si la respuesta es exitosa
            Log.d("LiberarVehiculo", "Vehículo liberado correctamente.")
        } else {
            // Si la respuesta no es exitosa
            Log.e("LiberarVehiculo", "Error al liberar vehículo: ${response.message()}")
        }
    } catch (e: Exception) {
        // Manejar cualquier excepción
        Log.e("LiberarVehiculo", "Excepción: ${e.message}")
    }
}






fun calculateDaysRemaining(currentDate: Long, estimatedDeliveryDate: Long): Long {
    val diffInMillis = estimatedDeliveryDate - currentDate
    return TimeUnit.MILLISECONDS.toDays(diffInMillis)
}