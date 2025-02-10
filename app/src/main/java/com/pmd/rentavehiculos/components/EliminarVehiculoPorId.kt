package com.pmd.rentavehiculos.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.pmd.rentavehiculos.data.model.LoginState
import com.pmd.rentavehiculos.viewModel.AutenticacionViewModel
import com.pmd.rentavehiculos.viewModel.VehiculoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EliminarVehiculoPorId(viewModel: VehiculoViewModel = viewModel()) {
    var id by remember { mutableStateOf("") } // Estado para el ID ingresado
    val vehiculos by viewModel.vehiculos.collectAsState() // Observar la lista de vehículos actualizada

    Scaffold(
        topBar = { TopAppBar(title = { Text("Eliminar Vehículo") }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = id,
                    onValueChange = { id = it },
                    label = { Text("ID del Vehículo") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        val idInt = id.toIntOrNull()
                        if (idInt != null) {
                            viewModel.eliminarVehiculoPorId(idInt)
                        } else {
                            println("ID inválido")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Eliminar Vehículo")
                }

                // Mostrar la lista de vehículos actualizada
                Spacer(modifier = Modifier.height(16.dp))
                Text("Vehículos Disponibles:", style = MaterialTheme.typography.titleMedium)
                vehiculos.forEach { vehiculo ->
                    Text("ID: ${vehiculo.id}, Marca: ${vehiculo.marca}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
