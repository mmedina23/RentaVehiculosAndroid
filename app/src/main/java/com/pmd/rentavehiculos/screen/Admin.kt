package com.pmd.rentavehiculos.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun Admin(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) } // Estado para controlar si el diálogo está visible
    var vehiculoIdInput by remember { mutableStateOf("") } // Estado para almacenar el texto ingresado

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Mensaje de bienvenida
            Text(
                text = "Bienvenido Admin",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Botón para ver listado de vehículos disponibles
            Button(
                onClick = { navController.navigate("adminListado") }, // Navegación a adminListado
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Ver Listado de Vehículos Disponibles")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para abrir el diálogo
            Button(
                onClick = { showDialog = true }, // Mostrar el diálogo al hacer clic
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Ver Listado de Vehículos Rentados")
            }
        }
    }

    // Diálogo
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false }, // Cerrar el diálogo al hacer clic afuera
            confirmButton = {
                TextButton(onClick = {
                    // Aquí puedes manejar el evento de confirmación
                    showDialog = false
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Buscar Vehículo Rentado") },
            text = {
                Column {
                    TextField(
                        value = vehiculoIdInput,
                        onValueChange = { vehiculoIdInput = it },
                        label = { Text("ID del Vehículo") },
                        placeholder = { Text("Escribe un id...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        )
    }
}
