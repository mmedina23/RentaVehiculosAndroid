package com.pmd.rentavehiculos.ui.theme.admin
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.data.model.Vehiculo
import com.pmd.rentavehiculos.ui.theme.viewmodel.AdminViewModel

@Composable
fun ListaVehiculosDisponibles(navController: NavController, context: Context) {
    val viewModel: AdminViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                return AdminViewModel(context) as T
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.loadVehiculosDisponibles()
    }

    val vehiculosDisponibles by viewModel.vehiculosDisponibles.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // Fondo negro sólido
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🚘 Vehículos Disponibles", style = MaterialTheme.typography.headlineMedium, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))

        if (vehiculosDisponibles.isEmpty()) {
            Text("No hay vehículos disponibles", style = MaterialTheme.typography.bodyMedium, color = Color.White)
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(vehiculosDisponibles) { vehiculo ->
                    VehiculoDisponibleCard(vehiculo) { vehiculoId ->
                        navController.navigate("historial_rentas/$vehiculoId")
                    }
                    Divider(thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp), color = Color.White)
                }
            }
        }
    }
}

@Composable
fun VehiculoDisponibleCard(vehiculo: Vehiculo, onVerHistorialClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(10.dp, RoundedCornerShape(16.dp))
            .border(2.dp, Color.White, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = vehiculo.imagen,
                contentDescription = "Imagen del vehículo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Marca: ${vehiculo.marca}", style = MaterialTheme.typography.bodyLarge, color = Color.White)
            Text("Modelo: ${vehiculo.carroceria}", color = Color.White)
            Text("Color: ${vehiculo.color}", color = Color.White)
            Text("Plazas: ${vehiculo.plazas}", color = Color.White)
            Text("Tipo Combustible: ${vehiculo.tipo_combustible}", color = Color.White)
            Text("Valor por Día: $${vehiculo.valor_dia}", color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onVerHistorialClick(vehiculo.id) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text("Ver Historial", color = Color.White)
            }
        }
    }
}
