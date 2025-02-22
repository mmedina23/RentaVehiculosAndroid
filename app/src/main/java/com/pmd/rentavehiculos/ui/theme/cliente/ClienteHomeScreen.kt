package com.pmd.rentavehiculos.ui.theme.cliente

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.data.model.Vehiculo
import com.pmd.rentavehiculos.ui.theme.viewmodel.ClienteViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ClienteHomeScreen(
    viewModel: ClienteViewModel,
    navController: NavController,
    onLogoutSuccess: () -> Unit
) {
    val vehiculosDisponibles by viewModel.vehiculosDisponibles.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.cargarVehiculosDisponibles()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // 🔹 Fondo negro sólido
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔹 Sección de botones alineados en la parte superior
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.navigate("rentas_actuales") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)), // 🔹 Azul más moderno
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ver Rentas Actuales", color = Color.White)
            }

            Button(
                onClick = {
                    viewModel.logout(
                        onLogoutSuccess = { onLogoutSuccess() },
                        onLogoutError = { errorMessage -> println(errorMessage) }
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cerrar Sesión", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Texto de "Vehículos Disponibles" bien visible
        Text(
            "Vehículos Disponibles",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(vehiculosDisponibles) { vehiculo ->
                VehiculoDisponibleCard(vehiculo) {
                    navController.navigate("detalle_vehiculo/${vehiculo.id}")
                }
                Divider(thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp), color = Color.White)
            }
        }
    }
}@Composable
fun VehiculoDisponibleCard(vehiculo: Vehiculo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
            .shadow(10.dp, RoundedCornerShape(16.dp)) // 🔹 Sombra para efecto elevado
            .border(2.dp, Color.White, RoundedCornerShape(16.dp)), // 🔹 Borde blanco para elegancia
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent) // 🔹 Fondo transparente
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            AsyncImage(
                model = vehiculo.imagen,
                contentDescription = "Imagen del vehículo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // 🔹 Asegura que todas las imágenes tengan el mismo tamaño
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Marca: ${vehiculo.marca}", color = Color.White, style = MaterialTheme.typography.bodyLarge)
            Text("Modelo: ${vehiculo.carroceria}", color = Color.White, style = MaterialTheme.typography.bodyLarge)
            Text(
                "Valor por Día: $${"%.2f".format(vehiculo.valor_dia)}",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}




