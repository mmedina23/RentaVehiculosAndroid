package com.pmd.rentavehiculos.screens

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

@Composable
fun DetalleVehiculoScreen(
    vehiculo: Vehiculo,
    apiKey: String,
    personaId: Int,
    onRentSuccess: () -> Unit,
    onBack: () -> Unit,
    vehiculosViewModel: VehiculosViewModel = viewModel()
) {
    var diasRenta by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    val errorMessage by vehiculosViewModel.errorLiveData.observeAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Detalle del Vehículo")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Marca: ${vehiculo.marca}")
        Text("Color: ${vehiculo.color}")
        Text(text = "Carrocería: ${vehiculo.carroceria}")
        Text(text = "Plazas: ${vehiculo.plazas}")
        Text(text = "Cambios: ${vehiculo.cambios}")
        Text(text = "Tipo de combustible: ${vehiculo.tipo_combustible}")
        Text(text = "Valor por día: ${vehiculo.valor_dia}")
        //El id no le interesa al usuario
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = diasRenta,
            onValueChange = { diasRenta = it },
            label = { Text("Días de Renta") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (errorMessage != null) {
            Text(text = errorMessage!!)
        }
        Button(
            onClick = {
                val dias = diasRenta.toIntOrNull()
                if (dias != null && dias > 0) {
                    isProcessing = true
                    // Llamamos a la función para rentar el vehículo.
                    vehiculosViewModel.rentarVehiculo(apiKey, vehiculo.id, personaId, dias) {
                        isProcessing = false
                        onRentSuccess()
                    }
                }
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