import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("👨‍💼 Bienvenido, Administrador", style = MaterialTheme.typography.headlineMedium, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))

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
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
        ) {
            Text("Cerrar Sesión", color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)) // Fondo oscuro elegante
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { navController.navigate("vehiculos_disponibles") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver Vehículos Disponibles", color = Color.White)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { navController.navigate("vehiculos_rentados") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver Vehículos Rentados", color = Color.White)
                }
            }
        }
    }
}
