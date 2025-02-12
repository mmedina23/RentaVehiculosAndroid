package com.pmd.rentavehiculos.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pmd.rentavehiculos.modelo.Vehiculo
import com.pmd.rentavehiculos.viewModels.VehiculosViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import com.pmd.rentavehiculos.data.SessionManager
import com.pmd.rentavehiculos.modelo.PersonaRequest
import com.pmd.rentavehiculos.modelo.RentarVehiculoRequest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Composable
fun DetalleVehiculoScreen(
    vehiculo: Vehiculo,
    apiKey: String,
    personaId: Int,
    onRentSuccess: () -> Unit,
    onBack: () -> Unit,
    sessionManager: SessionManager,
    vehiculosViewModel: VehiculosViewModel = viewModel()
) {
    var diasRenta by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    val errorMessage by vehiculosViewModel.errorLiveData.observeAsState()
    // Convertir el texto a entero y calcular el total
    val diasInt = diasRenta.toIntOrNull() ?: 0
    val totalRenta = diasInt * vehiculo.valor_dia

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Detalle del Vehículo", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Marca: ${vehiculo.marca}")
        Text("Color: ${vehiculo.color}")
        Text("Carrocería: ${vehiculo.carroceria}")
        Text("Plazas: ${vehiculo.plazas}")
        Text("Cambios: ${vehiculo.cambios}")
        Text("Tipo de combustible: ${vehiculo.tipo_combustible}")
        Text("Valor por día: ${vehiculo.valor_dia}")
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = diasRenta,
            onValueChange = { diasRenta = it },
            label = { Text("Días de Renta") },
            modifier = Modifier.fillMaxWidth()
        )
        if (diasInt > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Total a pagar: $totalRenta", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage != null) {
            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = {
                val dias = diasRenta.toIntOrNull()
                if (dias == null || dias <= 0) {
                    vehiculosViewModel.errorLiveData.value = "Ingrese una cantidad válida de días"
                    return@Button
                }

                // Obtén los datos reales del usuario desde el SessionManager
                val persona = sessionManager.persona
                if (persona == null) {
                    vehiculosViewModel.errorLiveData.value = "No se encontró información del usuario."
                    return@Button
                }

                // Crea el objeto PersonaRequest incluyendo solo el id
                val personaRequest = PersonaRequest(id = persona.id.toString())

                // Calcula el total y formatea las fechas
                val valorTotal = (vehiculo.valor_dia * dias).toInt()
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }
                val calendar = Calendar.getInstance()
                val fechaRenta = sdf.format(calendar.time)
                calendar.add(Calendar.DAY_OF_YEAR, dias)
                val fechaEstimadaEntrega = sdf.format(calendar.time)
                Log.d("FECHA", fechaEstimadaEntrega)

                // Crea el objeto RentarVehiculoRequest sin incluir información del vehículo
                val request = RentarVehiculoRequest(
                    persona = personaRequest,
                    dias_renta = dias,
                    valor_total_renta = valorTotal,
                    fecha_renta = fechaRenta,
                    fecha_estimada_entrega = fechaEstimadaEntrega
                )

                // Imprime el JSON generado para depurar
                val jsonRequest = Gson().toJson(request)
                Log.d("REQUEST_JSON", jsonRequest)

                isProcessing = true
                vehiculosViewModel.rentarVehiculo(
                    apiKey = apiKey,
                    usuario = persona, // <-- Aquí pasas el usuario (persona)
                    vehiculoId = vehiculo.id,
                    request = request,
                    onSuccess = {
                        isProcessing = false
                        onRentSuccess()
                    },
                    onError = { mensaje ->
                        isProcessing = false
                        // Puedes mostrar el mensaje de error recibido, por ejemplo, en un Snackbar.
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isProcessing
        ) {
            if (isProcessing) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Rentar Vehículo")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Regresar")
        }
    }
}