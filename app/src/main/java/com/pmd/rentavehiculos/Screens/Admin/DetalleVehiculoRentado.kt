package com.pmd.rentavehiculos.Screens.Admin

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.pmd.rentavehiculos.Api.RetrofitInstance
import com.pmd.rentavehiculos.Entity.RentaAdmin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun vehiculoRentadoDetalle(token: String) {
    var rentas by remember { mutableStateOf<List<RentaAdmin>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var vehiculoId by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campo de texto para ingresar el ID del vehículo
        OutlinedTextField(
            value = vehiculoId,
            onValueChange = { vehiculoId = it },
            label = { Text("Ingrese el ID del vehículo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para confirmar
        Button(
            onClick = {
                if (vehiculoId.isNotEmpty()) {
                    isLoading = true
                    coroutineScope.launch(Dispatchers.IO) {
                        try {
                            val service = RetrofitInstance.makeRetrofitService()
                            val response = service.obtenerDetalleVehiculo(id = vehiculoId.toInt(), token = token)
                            withContext(Dispatchers.Main) {
                                rentas = response
                                errorMessage = null
                                isLoading = false
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                errorMessage = "Error obteniendo vehículos: ${e.message}"
                                Log.e("VehiculoRentadoDetalle", "Error: ${e.message}")
                                isLoading = false
                            }
                        }
                    }
                }
            },
            enabled = vehiculoId.isNotEmpty()
        ) {
            Text("Confirmar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Indicador de carga
        if (isLoading) {
            CircularProgressIndicator()
        }

        // Mostrar error si lo hay
        errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        // Mostrar los datos del vehículo rentado si están disponibles
        rentas?.let {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(it) { renta ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Imagen del vehículo
                            Image(
                                painter = rememberAsyncImagePainter(renta.vehiculo.imagen),
                                contentDescription = "Vehículo",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Datos de la persona
                            Text(text = "Rentado por: ${renta.persona.nombre} ${renta.persona.apellido}", fontSize = 18.sp)
                            Text(text = "Teléfono: ${renta.persona.telefono}")
                            Text(text = "Identificación: ${renta.persona.tipoIdentificacion} - ${renta.persona.identificacion}")

                            Spacer(modifier = Modifier.height(8.dp))

                            // Datos del alquiler
                            Text(text = "Días rentado: ${renta.dias_renta}", fontSize = 16.sp)
                            Text(text = "Fecha de renta: ${renta.fecha_renta}")
                            Text(text = "Fecha prevista de entrega: ${renta.fecha_estimada_entrega}")
                            Text(text = "Fecha de entrega: ${renta.fecha_entregado ?: "No entregado aún"}")
                            Text(text = "Valor total: \$${renta.valor_total_renta}", fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}
