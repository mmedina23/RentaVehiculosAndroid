package com.pmd.rentavehiculos.navigation

import AdminHomeScreen
import android.content.Context
import androidx.compose.runtime.Composable
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
import com.pmd.rentavehiculos.ui.theme.admin.HistorialRentasScreen
import com.pmd.rentavehiculos.ui.theme.admin.ListaVehiculosDisponibles
import com.pmd.rentavehiculos.ui.theme.admin.ListaVehiculosRentados
import com.pmd.rentavehiculos.ui.theme.cliente.ClienteHomeScreen
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
    val sessionManager = SessionManager(context)

    // Crear una única instancia de ClienteViewModel para evitar repetición
    val clienteViewModelFactory = ClienteViewModelFactory(rentaService, vehiculoService, sessionManager)
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
            AdminHomeScreen(navController = navController, context = context)
        }

        composable("vehiculos_disponibles") {
            ListaVehiculosDisponibles(navController = navController, context = context)
        }

        composable("vehiculos_rentados") {
            val adminViewModelFactory = AdminViewModelFactory(context)
            val adminViewModel: AdminViewModel = ViewModelProvider(it, adminViewModelFactory)[AdminViewModel::class.java]

            ListaVehiculosRentados(navController = navController, viewModel = adminViewModel)
        }


        composable("cliente_home") {
            ClienteHomeScreen(viewModel = clienteViewModel, navController = navController)
        }

        composable("rentas_actuales") {
            RentasActualesScreen(viewModel = clienteViewModel, navController = navController)
        }

        composable("historial_rentas/{vehiculoId}") { backStackEntry ->
            val vehiculoId = backStackEntry.arguments?.getString("vehiculoId")?.toIntOrNull()
            val context = LocalContext.current  // Obtén el contexto actual

            if (vehiculoId != null) {
                val adminViewModel: AdminViewModel = viewModel(
                    factory = AdminViewModelFactory(context) // Usa el factory correctamente
                )

                HistorialRentasScreen(
                    viewModel = adminViewModel,
                    vehiculoId = vehiculoId,
                    navController = navController
                )
            }
        }
    }
}
