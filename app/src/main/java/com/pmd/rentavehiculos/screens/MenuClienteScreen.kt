import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.pmd.rentavehiculos.classes.Vehiculo
import com.pmd.rentavehiculos.classes.RentRequest
import com.pmd.rentavehiculos.viewmodels.LoginViewModel
import com.pmd.rentavehiculos.viewmodels.VehiculosViewModel

@Composable
fun MenuClienteScreen(
    navController: NavController,
    loginVM: LoginViewModel,
    vehiculosVM: VehiculosViewModel
) {
    // las dos tabs.
    val tabs = listOf("Vehículos Disponibles", "Vehículos Rentados")
    var selectedTabIndex by remember { mutableStateOf(0) }

    // obtiene la Key.
    val apiKey = loginVM.key.value ?: ""
    val clientId = loginVM.user.value?.id ?: 0 //asume 0 si es null el ID

    // Si cambia el ID o la API key vuelve a fetchear.
    LaunchedEffect(apiKey, clientId) {
        vehiculosVM.fetchVehiculosDisponibles(apiKey)
        vehiculosVM.fetchRentasCliente(apiKey, clientId)
    }

    Scaffold(
        topBar = {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }
        }
    ) { paddingValues ->
        // Contenido bajo las tabs.
        when (selectedTabIndex) {
            0 -> ListaDisponibles(
                vehiculos = vehiculosVM.vehiculosDisponibles.value,
                modifier = Modifier.padding(paddingValues)
            )
            1 -> ListaRentados(
                rentas = vehiculosVM.rentasCliente.value,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun ListaDisponibles(vehiculos: List<Vehiculo>, modifier: Modifier = Modifier) {
    if (vehiculos.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay vehículos disponibles")
        }
    } else {
        LazyColumn(modifier = modifier) {
            items(vehiculos) { vehiculo ->
                VehicleCard(vehiculo = vehiculo)
            }
        }
    }
}

@Composable
fun ListaRentados(rentas: List<RentRequest>, modifier: Modifier = Modifier) {
    if (rentas.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No tienes ningún vehículo rentado")
        }
    } else {
        LazyColumn(modifier = modifier) {
            items(rentas) { rental ->
                VehicleCard(vehiculo = rental.vehiculo)
            }
        }
    }
}

@Composable
fun VehicleCard(vehiculo: Vehiculo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        // [Changed from "elevation = 4.dp" to use Material 3 CardElevation type]
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Display the vehicle's image.
            Image(
                painter = rememberImagePainter(data = vehiculo.imagen),
                contentDescription = "Imagen del vehículo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = vehiculo.marca,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = vehiculo.color,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
