package com.pmd.rentavehiculos.views

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.pmd.rentavehiculos.models.VehiculoRentado
import com.pmd.rentavehiculos.viewmodels.VehiculosRentadosViewModel

class VehiculosRentadosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VehiculosRentadosScreen()
        }
    }
}
@Composable
fun VehiculosRentadosScreen(viewModel: VehiculosRentadosViewModel = viewModel()) {
    val context = LocalContext.current

    LaunchedEffect(Unit) { viewModel.cargarVehiculosRentados(1) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Vehículos Rentados") }) }
    ) { padding ->
        if (viewModel.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(viewModel.vehiculosRentados) { vehiculo ->
                    VehiculoRentadoItem(vehiculo) {
                        viewModel.devolverVehiculo(1, vehiculo.id) { mensaje ->
                            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun VehiculoRentadoItem(vehiculo: VehiculoRentado, onDevolver: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Image(
                    painter = rememberImagePainter(vehiculo.imagenUrl),
                    contentDescription = "Imagen del vehículo",
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(text = "${vehiculo.marca} ${vehiculo.modelo}", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Renta desde: ${vehiculo.fechaRenta}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Entrega prevista: ${vehiculo.fechaEntrega}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Total pagado: \$${vehiculo.precioTotal}", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = { showDialog = true }) {
                Text("Devolver Vehículo")
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Confirmar Devolución") },
                    text = { Text("¿Estás seguro de que quieres devolver este vehículo?") },
                    confirmButton = {
                        Button(onClick = {
                            onDevolver()
                            showDialog = false
                        }) {
                            Text("Sí, devolver")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}
