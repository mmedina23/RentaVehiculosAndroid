package com.pmd.rentavehiculos.components


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.*  // Importante para usar Material 3 components como Text, OutlinedTextField, Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pmd.rentavehiculos.data.model.Vehiculo
import com.pmd.rentavehiculos.viewModel.VehiculoViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun IngresarVehiculo(viewModel: VehiculoViewModel = viewModel()) {
    val context = LocalContext.current

    // Variables de estado para almacenar los datos del formulario
    var marca by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var carroceria by remember { mutableStateOf("") }
    var plazas by remember { mutableStateOf("") }
    var cambios by remember { mutableStateOf("") }
    var tipoCombustible by remember { mutableStateOf("") }
    var valorDia by remember { mutableStateOf("") }
    var disponible by remember { mutableStateOf(false) }  // Usaremos un switch para esto

    // Estructura del formulario
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OutlinedTextField(
                value = marca,
                onValueChange = { marca = it },
                label = { Text("Marca") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = color,
                onValueChange = { color = it },
                label = { Text("Color") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = carroceria,
                onValueChange = { carroceria = it },
                label = { Text("Carrocería") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = plazas,
                onValueChange = { plazas = it },
                label = { Text("Plazas") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = cambios,
                onValueChange = { cambios = it },
                label = { Text("Cambios") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = tipoCombustible,
                onValueChange = { tipoCombustible = it },
                label = { Text("Tipo de Combustible") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            OutlinedTextField(
                value = valorDia,
                onValueChange = { valorDia = it },
                label = { Text("Valor por Día") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Switch para disponibilidad
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Disponible")
                Switch(checked = disponible, onCheckedChange = { disponible = it })
            }
        }

        // Botón para enviar el formulario
        item {
            Button(
                onClick = {
                    try {
                        // Convertir plazas y valorDia a números
                        val plazasInt = plazas.toIntOrNull() ?: throw IllegalArgumentException("Plazas debe ser un número")
                        val valorDiaDouble = valorDia.toDoubleOrNull() ?: throw IllegalArgumentException("Valor por Día debe ser un número")

                        // Crear el objeto Vehiculo
                        val nuevoVehiculo = Vehiculo(
                            marca = marca,
                            color = color,
                            carroceria = carroceria,
                            plazas = plazasInt,
                            cambios = cambios,
                            tipoCombustible = tipoCombustible,
                            valorDia = valorDiaDouble,
                            disponible = disponible
                        )

                        // Enviar al ViewModel
                        viewModel.ingresoVehiculo(nuevoVehiculo)

                        // Limpiar el formulario
                        marca = ""
                        color = ""
                        carroceria = ""
                        plazas = ""
                        cambios = ""
                        tipoCombustible = ""
                        valorDia = ""
                        disponible = false

                        Toast.makeText(context, "Vehículo ingresado exitosamente.", Toast.LENGTH_SHORT).show()

                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar Vehículo")
            }
        }
    }
}

