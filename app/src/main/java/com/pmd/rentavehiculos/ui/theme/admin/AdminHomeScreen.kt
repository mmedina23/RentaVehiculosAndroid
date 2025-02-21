import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.ui.theme.viewmodel.AdminViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    navController: NavController,
    context: Context,
    onLogoutSuccess: () -> Unit
) {
    val viewModel: AdminViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AdminViewModel(context) as T
            }
        }
    )

    // Obtenemos el estado de los vehículos disponibles y rentados
    val vehiculosDisponibles by viewModel.vehiculosDisponibles.observeAsState(emptyList())
    val vehiculosRentados by viewModel.vehiculosRentadosAdminLiveData.observeAsState(emptyList())

    // Cargar los vehículos rentados solo una vez
    LaunchedEffect(Unit) {
        viewModel.loadVehiculosRentadosAdmin()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("👨‍💼 Bienvenido, Administrador", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { navController.navigate("vehiculos_disponibles") }) {
                Text("Ver Vehículos Disponibles")
            }
            Button(onClick = { navController.navigate("vehiculos_rentados") }) {
                Text("Ver Vehículos Rentados")
            }
        }

        TopAppBar(
            title = { Text("Renta de Vehículos") },
            actions = {
                Button(
                    onClick = {
                        viewModel.logout(
                            onLogoutSuccess = {
                                navController.navigate("login") {
                                    popUpTo("admin_home") { inclusive = true }
                                }
                            },
                            onLogoutError = { errorMessage -> println(errorMessage) }
                        )
                    }
                ) {
                    Text("Cerrar Sesión")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
