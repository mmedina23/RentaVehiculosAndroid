package com.pmd.rentavehiculos.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.Alignment
import com.pmd.rentavehiculos.screens.AdminMenuScreen
import com.pmd.rentavehiculos.screens.RentaVehiculosListScreen
import com.pmd.rentavehiculos.screens.RentasAdminScreen
import com.pmd.rentavehiculos.screens.VehiculosDisponiblesAdminScreen

class AdminActivity : ComponentActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)

        val personaId: Int = sessionManager.personaId ?: 0
        val apiKey: String = sessionManager.token ?: ""
        Log.d("AdminActivity", "Se inició AdminActivity")

        setContent {
            RentaVehiculosTheme {
                Scaffold { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "admin_menu",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Pantalla de menú de administración
                        composable("admin_menu") {
                            AdminMenuScreen(
                                onVerDisponibles = { navController.navigate("vehiculos_disponibles") },
                                onVerRentados = { navController.navigate("rentas") },
                                //onGestionVehiculos = { /* Navega a la pantalla de gestió */ },
                                onLogoutClick = {
                                    // Limpia la sesión y redirige al login
                                    sessionManager.clearSession()
                                    val intent = Intent(this@AdminActivity, LoginActivity::class.java).apply {
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    }
                                    startActivity(intent)
                                    finish()
                                }
                            )
                        }
                        // Pantalla de vehículos disponibles para admin
                        composable("vehiculos_disponibles") {
                            VehiculosDisponiblesAdminScreen(
                                apiKey = apiKey,
                                onBackClick = { navController.popBackStack() },
                                onVehiculoClick = { vehiculo ->
                                    navController.navigate("detalle_vehiculo/${vehiculo.id}")
                                }
                            )
                        }

                        composable(
                            route = "detalle_vehiculo/{vehiculoId}",
                            arguments = listOf(
                                navArgument("vehiculoId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val vehiculoId = backStackEntry.arguments?.getInt("vehiculoId") ?: 0
                            Log.d("DETALLE_ADMIN", "Detalle solicitado para vehículo id: $vehiculoId")

                            // Obtén el ViewModel (puedes usar el mismo VehiculosViewModel o uno propio)
                            val vehiculosViewModel: VehiculosViewModel = viewModel()

                            // Llama a la función para obtener el detalle del vehículo
                            LaunchedEffect(key1 = vehiculoId) {
                                vehiculosViewModel.obtenerDetalleVehiculo(apiKey, vehiculoId)
                            }

                            // Observa el LiveData del detalle del vehículo
                            val vehiculo: Vehiculo? by vehiculosViewModel.vehiculoDetalleLiveData.observeAsState(initial = null)
                            val vehiculoLocal = vehiculo
                            if (vehiculoLocal == null) {
                                Log.d("DETALLE_ADMIN", "Vehículo aún nulo, mostrando indicador de carga")
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator()
                                }
                            } else {
                                Log.d("DETALLE_ADMIN", "Detalle del vehículo obtenido: ${vehiculoLocal.id}")
                                DetalleVehiculoScreen(
                                    vehiculo = vehiculoLocal,
                                    apiKey = apiKey,
                                    personaId = personaId,
                                    sessionManager = sessionManager,
                                    onRentSuccess = { navController.popBackStack() },
                                    onBack = { navController.popBackStack() },
                                    isAdmin = true, // Especifica que es para admin
                                    // Si en el futuro el admin tiene acciones propias (editar/eliminar), se activarán en función de este flag.
                                )
                            }
                        }
                        // Pantalla de lista de vehículos rentados
                        composable("rentas") {
                            RentaVehiculosListScreen(
                                apiKey = apiKey,
                                onVehicleClick = { vehiculoId ->
                                    navController.navigate("rentas/$vehiculoId")
                                },
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        // Pantalla de historial de rentas de un vehículo específico
                        composable(
                            route = "rentas/{vehiculoId}",
                            arguments = listOf(navArgument("vehiculoId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val vehiculoId = backStackEntry.arguments?.getInt("vehiculoId") ?: 0
                            RentasAdminScreen(
                                apiKey = apiKey,
                                vehiculoId = vehiculoId,
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                    }
                }
            }
        }
    }
}
