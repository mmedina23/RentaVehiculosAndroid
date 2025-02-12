package com.pmd.rentavehiculos.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmd.rentavehiculos.modelo.RentaVehiculo
import com.pmd.rentavehiculos.viewModels.VehiculosRentadosViewModel

@Composable
fun VehiculosRentadosScreen(
    apiKey: String,
    personaId: Int,
    onEntregarVehiculo: (Int) -> Unit,
    onBack: () -> Unit,
    rentadosViewModel: VehiculosRentadosViewModel = viewModel()
) {
    // Se consulta la API una sola vez cuando se carga la pantalla
    LaunchedEffect(Unit) {
        rentadosViewModel.obtenerVehiculosRentados(apiKey, personaId)
    }

    // Especificamos el tipo vacío para evitar inferencias erróneas
    val rentas by rentadosViewModel.vehiculosRentadosLiveData.observeAsState(emptyList<RentaVehiculo>())
    val errorMessage by rentadosViewModel.errorLiveData.observeAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Button(onClick = onBack) {
            Text("Regresar")
        }
        Spacer(modifier = Modifier.height(16.dp))
        when {
            errorMessage != null -> {
                Text(text = errorMessage!!)
            }
            rentas.isEmpty() -> {
                // Mientras se carga o si la lista está vacía, muestra un indicador
                CircularProgressIndicator()
            }
            else -> {
                LazyColumn {
                    items(rentas) { renta ->
                        RentadoItem(renta = renta, onEntregar = {
                            // Llamada a la función del view model para entregar el vehículo
                            rentadosViewModel.entregarVehiculo(apiKey, renta.vehiculo.id)
                            // Refrescar la lista luego de entregar
                            rentadosViewModel.obtenerVehiculosRentados(apiKey, personaId)
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun RentadoItem(renta: RentaVehiculo, onEntregar: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Vehículo: ${renta.vehiculo.marca}")
            Text("Días de Renta: ${renta.dias}")
            Text("Fecha de Renta: ${renta.fechaRenta}")
            Text("Fecha Estimada Entrega: ${renta.fechaPrevistaEntrega}")
            Text("Valor Total: ${renta.valorTotal}")
            if (renta.fechaEntrega == null) {
                Button(
                    onClick = onEntregar,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Entregar Vehículo")
                }
            } else {
                Text("Vehículo entregado", modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}