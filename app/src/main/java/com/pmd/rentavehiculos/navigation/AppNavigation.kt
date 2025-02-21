package com.pmd.rentavehiculos.navigation

import AdminHomeScreen
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pmd.rentavehiculos.data.repository.RetrofitClient
import com.pmd.rentavehiculos.data.repository.SessionManager
import com.pmd.rentavehiculos.ui.auth.LoginScreen
import com.pmd.rentavehiculos.ui.theme.admin.DetalleVehiculoRentadoScreen
import com.pmd.rentavehiculos.ui.theme.admin.HistorialRentasScreen
import com.pmd.rentavehiculos.ui.theme.admin.ListaVehiculosDisponibles
import com.pmd.rentavehiculos.ui.theme.admin.ListaVehiculosRentados
import com.pmd.rentavehiculos.ui.theme.cliente.ClienteHomeScreen
import com.pmd.rentavehiculos.ui.theme.cliente.DetalleVehiculoScreen
import com.pmd.rentavehiculos.ui.theme.cliente.RentasActualesScreen
import com.pmd.rentavehiculos.ui.theme.home.HomeScreen
import com.pmd.rentavehiculos.ui.theme.viewmodel.AdminViewModel
import com.pmd.rentavehiculos.ui.theme.viewmodel.AdminViewModelFactory
import com.pmd.rentavehiculos.ui.theme.viewmodel.ClienteViewModel
import com.pmd.rentavehiculos.ui.theme.viewmodel.ClienteViewModelFactory

@Composable
fun AppNavigation(
    navController: NavHostController,
    context: Context,
    modifier: Modifier = Modifier
) {
    val rentaService = RetrofitClient.rentaService
    val vehiculoService = RetrofitClient.vehiculoService
    val authService = RetrofitClient.authService  // 🔹 Se obtiene `AuthService`
    val sessionManager = remember { SessionManager(context) }

    // Crear ClienteViewModelFactory con todas las dependencias necesarias
    val clienteViewModelFactory = ClienteViewModelFactory(rentaService, vehiculoService, authService, sessionManager)
    val clienteViewModel: ClienteViewModel = viewModel(factory = clienteViewModelFactory)

    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(navController = navController, context = context)
        }

        composable("home") { HomeScreen() }

        composable("admin_home") {
            AdminHomeScreen(
                navController = navController,
                context = context,
                onLogoutSuccess = {
                    sessionManager.clearSession() // Limpia la sesión
                    navController.navigate("login") {
                        popUpTo("cliente_home") { inclusive = true } // Borra historial para evitar volver atrás
                    }
                }
                )
        }

        composable("vehiculos_disponibles") {
            ListaVehiculosDisponibles(navController = navController, context = context)
        }

        composable("vehiculos_rentados") {
            val adminViewModelFactory = AdminViewModelFactory(context)
            val adminViewModel: AdminViewModel = viewModel(factory = adminViewModelFactory)

            ListaVehiculosRentados(navController = navController, viewModel = adminViewModel)
        }

        composable("cliente_home") {
            ClienteHomeScreen(
                viewModel = clienteViewModel,
                navController = navController,
                onLogoutSuccess = {
                    sessionManager.clearSession() // Limpia la sesión
                    navController.navigate("login") {
                        popUpTo("cliente_home") { inclusive = true } // Borra historial para evitar volver atrás
                    }
                }
            )
        }


        composable("rentas_actuales") {
            RentasActualesScreen(viewModel = clienteViewModel, navController = navController)
        }

        composable("detalle_vehiculo/{vehiculoId}") { backStackEntry ->
            val vehiculoId = backStackEntry.arguments?.getString("vehiculoId") ?: return@composable
            DetalleVehiculoScreen(vehiculoId, viewModel = clienteViewModel, navController)
        }

        composable("historial_rentas/{vehiculoId}") { backStackEntry ->
            val vehiculoId = backStackEntry.arguments?.getString("vehiculoId")?.toIntOrNull()
            if (vehiculoId != null) {
                val adminViewModel: AdminViewModel = viewModel(factory = AdminViewModelFactory(context))

                HistorialRentasScreen(
                    viewModel = adminViewModel,
                    vehiculoId = vehiculoId,
                    navController = navController
                )
            }
        }

        composable("detalle_vehiculo_rentado/{vehiculoId}") { backStackEntry ->
            val vehiculoId = backStackEntry.arguments?.getString("vehiculoId")?.toIntOrNull()
            if (vehiculoId != null) {
                val adminViewModel: AdminViewModel = viewModel(factory = AdminViewModelFactory(context))

                DetalleVehiculoRentadoScreen(
                    navController = navController,
                    viewModel = adminViewModel,
                    vehiculoId = vehiculoId
                )
            }
        }
    }
}
