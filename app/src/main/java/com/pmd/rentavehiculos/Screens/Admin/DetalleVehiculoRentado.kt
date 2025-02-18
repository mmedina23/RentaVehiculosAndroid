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
import androidx.compose.ui.draw.clip
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
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campo de texto para ingresar el ID del vehículo
        OutlinedTextField(
            value = vehiculoId,
            onValueChange = { vehiculoId = it },
            label = { Text("Ingrese el ID del vehículo", style = MaterialTheme.typography.bodyMedium) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            ),
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(24.dp))

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
            enabled = vehiculoId.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Confirmar", style = MaterialTheme.typography.labelLarge)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Indicador de carga
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Mostrar error si lo hay
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Mostrar los datos del vehículo rentado si están disponibles
        rentas?.let {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(it) { renta ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Imagen del vehículo
                            Image(
                                painter = rememberAsyncImagePainter(renta.vehiculo.imagen),
                                contentDescription = "Vehículo",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(MaterialTheme.shapes.medium)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Datos de la persona
                            Text(
                                text = "Rentado por: ${renta.persona.nombre} ${renta.persona.apellido}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Teléfono: ${renta.persona.telefono}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "Identificación: ${renta.persona.tipoIdentificacion} - ${renta.persona.identificacion}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Datos del alquiler
                            Text(
                                text = "Días rentado: ${renta.dias_renta}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Fecha de renta: ${renta.fecha_renta}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "Fecha prevista de entrega: ${renta.fecha_estimada_entrega}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "Fecha de entrega: ${renta.fecha_entregado ?: "No entregado aún"}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "Valor total: \$${renta.valor_total_renta}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}