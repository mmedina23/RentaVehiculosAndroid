package com.pmd.rentavehiculos.activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
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
                                }
                            )
                        }
                        // Pantalla de vehículos disponibles
                        composable("vehiculos_disponibles") {
                            VehiculosDisponiblesScreen(
                                apiKey = apiKey,
                                onVehiculoClick = { vehiculo ->
                                    // Navega al detalle pasando el id del vehículo
                                    navController.navigate("detalle_vehiculo/${vehiculo.id}")
                                }
                            )
                        }
                        // Pantalla de detalle de vehículo
                        composable(
                            route = "detalle_vehiculo/{vehiculoId}",
                            arguments = listOf(
                                navArgument("vehiculoId") {
                                    this.type = NavType.IntType
                                }
                            )
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("vehiculoId") ?: 0
                            // Se crea un objeto Vehiculo simulado; en producción se consulta el ViewModel
                            val vehiculoSimulado = Vehiculo(
                                id = id,
                                marca = "Marca $id",
                                color = "Color",
                                carroceria = "TURISMO",
                                plazas = 4,
                                cambios = "AUTOMATICO",
                                tipo_combustible = "Gasolina",
                                valor_dia = 100.0,
                                disponible = true
                            )
                            DetalleVehiculoScreen(
                                vehiculo = vehiculoSimulado,
                                apiKey = apiKey,
                                personaId = personaId,
                                onRentSuccess = { navController.popBackStack() },
                                onBack = { navController.popBackStack() }
                            )
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