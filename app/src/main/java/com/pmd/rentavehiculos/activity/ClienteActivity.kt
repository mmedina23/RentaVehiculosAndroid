package com.pmd.rentavehiculos.activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pmd.rentavehiculos.data.SessionManager
import com.pmd.rentavehiculos.modelo.Vehiculo
import com.pmd.rentavehiculos.screens.ClientMenuScreen
import com.pmd.rentavehiculos.screens.DetalleVehiculoScreen
import com.pmd.rentavehiculos.screens.VehiculosDisponiblesScreen
import com.pmd.rentavehiculos.screens.VehiculosRentadosScreen
import com.pmd.rentavehiculos.ui.theme.RentaVehiculosTheme
import com.pmd.rentavehiculos.viewModels.VehiculosViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState


class ClienteActivity : ComponentActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)

        val personaId: Int = sessionManager.personaId ?: 0
        // Si token es nullable, asigna un valor por defecto (aunque lo ideal es que sea no nulo)
        val apiKey: String = sessionManager.token ?: ""

        setContent {
            RentaVehiculosTheme {
                Scaffold { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "client_menu",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Pantalla de menú del cliente
                        composable("client_menu") {
                            ClientMenuScreen(
                                onVehiculosDisponiblesClick = {
                                    navController.navigate("vehiculos_disponibles")
                                },
                                onHistorialRentadosClick = {
                                    navController.navigate("vehiculos_rentados")
                                },
                                onLogoutClick = {
                                    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                                    sharedPreferences.edit().clear().apply()
                                    val intent = Intent(this@ClienteActivity, LoginActivity::class.java).apply {
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    }
                                    startActivity(intent)
                                    finish()
                                }
                            )
                        }
                        // Pantalla de vehículos disponibles
                        composable("vehiculos_disponibles") {
                            VehiculosDisponiblesScreen(
                                apiKey = apiKey,
                                onVehiculoClick = { vehiculo ->
                                    Log.d("NAVIGATION", "Navegando a detalle del vehículo id: ${vehiculo.id}")
                                    navController.navigate("detalle_vehiculo/${vehiculo.id}")
                                },
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        // Pantalla de detalle de vehículo (usando datos reales)
                        composable(
                            route = "detalle_vehiculo/{vehiculoId}",
                            arguments = listOf(
                                navArgument("vehiculoId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val vehiculoId = backStackEntry.arguments?.getInt("vehiculoId") ?: 0
                            Log.d("DETALLE", "Detalle solicitado para vehículo id: $vehiculoId")

                            // Obtén el ViewModel
                            val vehiculosViewModel: VehiculosViewModel = viewModel()

                            // Llama a la función para obtener el detalle del vehículo
                            LaunchedEffect(key1 = vehiculoId) {
                                vehiculosViewModel.obtenerDetalleVehiculo(apiKey, vehiculoId)
                            }

                            // Observa el LiveData del detalle del vehículo
                            val vehiculo: Vehiculo? by vehiculosViewModel.vehiculoDetalleLiveData.observeAsState(initial = null)

                            val vehiculoLocal = vehiculo
                            if (vehiculoLocal == null) {
                                Log.d("DETALLE", "Vehículo aún nulo, mostrando indicador de carga")
                                CircularProgressIndicator()
                            } else {
                                Log.d("DETALLE", "Detalle del vehículo obtenido: ${vehiculoLocal.id}")
                                DetalleVehiculoScreen(
                                    vehiculo = vehiculoLocal,
                                    apiKey = apiKey,
                                    personaId = personaId,
                                    sessionManager = sessionManager, // Pasar la instancia de SessionManager
                                    onRentSuccess = { navController.popBackStack() },
                                    onBack = { navController.popBackStack() }
                                )
                            }
                        }
                        // Pantalla de vehículos rentados
                        composable("vehiculos_rentados") {
                            VehiculosRentadosScreen(
                                apiKey = apiKey,
                                personaId = personaId,
                                onEntregarVehiculo = { vehiculoId ->
                                    // Aquí defines la acción para entregar un vehículo
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}