package com.pmd.rentavehiculos.views

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pmd.rentavehiculos.models.VehiculoRentado
import com.pmd.rentavehiculos.network.ApiClient
import com.pmd.rentavehiculos.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosRentadosAdminScreen(navController: NavController, token: String, personaId: Int) { // 🔹 Se recibe personaId
    val context = LocalContext.current
    var vehiculosRentados by remember { mutableStateOf<List<VehiculoRentado>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val apiService = ApiClient.retrofit.create(ApiService::class.java)

    LaunchedEffect(Unit) {
        val call = apiService.obtenerVehiculosRentados(token, personaId)  // 🔹 Ahora se pasa personaId correctamente
        call.enqueue(object : Callback<List<VehiculoRentado>> {
            override fun onResponse(call: Call<List<VehiculoRentado>>, response: Response<List<VehiculoRentado>>) {
                if (response.isSuccessful) {
                    vehiculosRentados = response.body() ?: emptyList()
                } else {
                    Toast.makeText(context, "Error al obtener vehículos rentados", Toast.LENGTH_SHORT).show()
                }
                isLoading = false
            }

            override fun onFailure(call: Call<List<VehiculoRentado>>, t: Throwable) {
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
        })
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Vehículos Rentados") })
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(vehiculosRentados) { vehiculoRentado ->
                    VehiculoRentadoAdminItem(vehiculoRentado)
                }
            }
        }
    }
}

@Composable
fun VehiculoRentadoAdminItem(vehiculoRentado: VehiculoRentado) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "ID: ${vehiculoRentado.id} - ${vehiculoRentado.marca}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Cliente: ${vehiculoRentado.clienteNombre ?: "Desconocido"}", style = MaterialTheme.typography.bodyMedium) // 🔹 Evita errores si `clienteNombre` es null
            Text(text = "Días rentados: ${vehiculoRentado.diasRentados}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Total: \$${vehiculoRentado.precioTotal}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
