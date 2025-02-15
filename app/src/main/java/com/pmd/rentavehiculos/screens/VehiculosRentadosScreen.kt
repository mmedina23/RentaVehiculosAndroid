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
                LazyColumn (modifier = Modifier.weight(1f)) {//modifier = Modifier.weight(1f) PARA QUE OCUPE TODO EL ESPACIO Y PODER HACER SCROLL
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
            Spacer(modifier = Modifier.height(20.dp))
            Text("Vehículo: ${renta.vehiculo.marca}")
            Text("Color: ${renta.vehiculo.color}")
            Text("Carrocería: ${renta.vehiculo.carroceria}")
            Text("Plazas: ${renta.vehiculo.plazas}")
            Text("Cambios: ${renta.vehiculo.cambios}")
            Text("Tipo de Combustible: ${renta.vehiculo.tipo_combustible}")
            Text("Valor por Día: ${renta.vehiculo.valor_dia}")
            Spacer(modifier = Modifier.height(20.dp))
            Text("Días de Renta: ${renta.dias}")
            Spacer(modifier = Modifier.height(20.dp))
            Text("Fecha de Renta: ${renta.fechaRenta}")
            Text("Fecha Estimada Entrega: ${renta.fechaPrevistaEntrega}")
            Spacer(modifier = Modifier.height(20.dp))
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