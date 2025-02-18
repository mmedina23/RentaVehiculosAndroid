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
import androidx.compose.material3.Surface
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.Api.RetrofitInstance
import com.pmd.rentavehiculos.Entity.Renta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ListaMisVehiculos(token: String, personaId: Int) {
    val vehiculosList = remember { mutableStateOf<List<Renta>>(emptyList()) }

    // Llamada a la API en un hilo de fondo
    LaunchedEffect(personaId) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val service = RetrofitInstance.makeRetrofitService()
                val response = service.obtenerMisVehiculos(personaId, token)
                withContext(Dispatchers.Main) {
                    vehiculosList.value = response // Actualiza el estado con la respuesta
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ListaMisVehiculos", "Error obteniendo vehículos: ${e.message}")
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        items(vehiculosList.value) { renta ->
            // Card con sombra, padding, y contenido animado
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .animateContentSize(), // Animación al cambiar el tamaño
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                // Contenido dentro de la tarjeta
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Imagen del vehículo
                    AsyncImage(
                        model = renta.vehiculo.imagen,
                        contentDescription = "Imagen del vehículo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(bottom = 12.dp),
                    )

                    // Título de la tarjeta con estilo
                    Text(
                        text = "Marca: ${renta.vehiculo.marca}",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                    Text(
                        text = "Color: ${renta.vehiculo.color}",
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                    Text(
                        text = "Carrocería: ${renta.vehiculo.carroceria}",
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                    Text(
                        text = "Plazas: ${renta.vehiculo.plazas}",
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                    Text(
                        text = "Valor del día: ${renta.vehiculo.precioDia} €",
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                    Text(
                        text = "Disponible: ${renta.vehiculo.disponible}",
                        modifier = Modifier.padding(bottom = 8.dp),
                    )

                    // Botón para más detalles
                    Button(
                        onClick = {
                            // Acción del botón, como navegar a los detalles de renta.
                            Log.d("ListaMisVehiculos", "Renta seleccionada: ${renta.vehiculo.marca}")
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Ver más detalles")
                    }
                }
            }
        }
    }
}
