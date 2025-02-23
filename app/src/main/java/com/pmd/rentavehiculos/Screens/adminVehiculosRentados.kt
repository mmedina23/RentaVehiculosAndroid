package com.pmd.rentavehiculos.Screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pmd.rentavehiculos.modelos.Renta
import com.pmd.rentavehiculos.viewmodels.VehiculosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun adminVehiculosRentadosScreen(
    navController: NavHostController,
    viewModel: VehiculosViewModel
) {
    val vehiculosRentados by viewModel.vehiculosRentados.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        if (vehiculosRentados.isEmpty()) {
            viewModel.fetchVehiculosRentadosAdmin() //llAMO a la función correcta para administradores
        }
    }

    Scaffold(

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Lista de Vehículos Rentados",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (vehiculosRentados.isEmpty()) {
                Text("No hay vehículos rentados", fontSize = 18.sp)
            } else {
                LazyColumn {
                    items(vehiculosRentados) { renta ->
                        RentaCard(renta) //muestro la información de cada renta
                    }
                }
            }
        }
    }
}





@Composable
fun RentaCard(renta: Renta) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Vehículo ID: ${renta.vehiculo.id} - ${renta.vehiculo.marca}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Text("Rentado por: ${renta.persona.nombre} ${renta.persona.apellidos}")
            Text("Días rentados: ${renta.dias_renta}")
            Text("Fecha de renta: ${renta.fecha_renta}")
            Text("Fecha estimada de entrega: ${renta.fecha_estimada_entrega}")

            val fechaEntrega = renta.fecha_estimada_entrega ?: "No entregado aún"
            Text("Fecha de entrega: $fechaEntrega")

            Text(
                text = "Valor total: ${renta.valor_total_renta}€",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}
