package com.pmd.rentavehiculos.navigation

import AdminHomeScreen
import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pmd.rentavehiculos.data.repository.RentaService
import com.pmd.rentavehiculos.data.repository.RetrofitClient
import com.pmd.rentavehiculos.data.repository.SessionManager
import com.pmd.rentavehiculos.data.repository.VehiculoService
import com.pmd.rentavehiculos.ui.auth.LoginScreen
import com.pmd.rentavehiculos.ui.theme.admin.ListaVehiculosDisponibles
import com.pmd.rentavehiculos.ui.theme.admin.ListaVehiculosRentados
import com.pmd.rentavehiculos.ui.theme.cliente.ClienteHomeScreen
import com.pmd.rentavehiculos.ui.theme.home.HomeScreen
import com.pmd.rentavehiculos.ui.theme.viewmodel.ClienteViewModel
import com.pmd.rentavehiculos.ui.theme.viewmodel.ClienteViewModelFactory


@Composable
fun AppNavigation(
    navController: NavHostController,
    context: Context,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(
                navController = navController,
                context = context // Pasamos el contexto
            )
        }
        composable("home") { HomeScreen() }

        composable(
            route = "admin_home",
        ) {
            AdminHomeScreen(
                navController = navController,
                context = context
            )
        }

        composable("vehiculos_disponibles") {
            ListaVehiculosDisponibles(navController = navController, context = context)
        }
        composable("vehiculos_rentados") {
            ListaVehiculosRentados(navController = navController, context = context)
        }

        composable("cliente_home") { backStackEntry ->
            val context = LocalContext.current
            val rentaService = RetrofitClient.rentaService
            val vehiculoService = RetrofitClient.vehiculoService
            val sessionManager = SessionManager(context)

            val viewModelFactory = ClienteViewModelFactory(rentaService, vehiculoService, sessionManager)
            val viewModel = ViewModelProvider(backStackEntry, viewModelFactory)[ClienteViewModel::class.java]

            ClienteHomeScreen(
                viewModel = viewModel,
                navController = navController,
                context = context
            )
        }




    }
}
