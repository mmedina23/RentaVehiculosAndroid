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
import com.pmd.rentavehiculos.models.Vehiculo
import com.pmd.rentavehiculos.network.ApiClient
import com.pmd.rentavehiculos.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehiculosDisponiblesAdminScreen(navController: NavController) {
    val context = LocalContext.current
    var vehiculos by remember { mutableStateOf<List<Vehiculo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val apiService = ApiClient.retrofit.create(ApiService::class.java)

    LaunchedEffect(Unit) {
        val call = apiService.obtenerVehiculos("TOKEN_ADMIN")
        call.enqueue(object : Callback<List<Vehiculo>> {
            override fun onResponse(call: Call<List<Vehiculo>>, response: Response<List<Vehiculo>>) {
                if (response.isSuccessful) {
                    vehiculos = response.body() ?: emptyList()
                } else {
                    Toast.makeText(context, "Error al obtener vehículos", Toast.LENGTH_SHORT).show()
                }
                isLoading = false
            }

            override fun onFailure(call: Call<List<Vehiculo>>, t: Throwable) {
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
        })
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Vehículos Disponibles") }) }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(vehiculos) { vehiculo ->
                    VehiculoAdminItem(vehiculo)
                }
            }
        }
    }
}

@Composable
fun VehiculoAdminItem(vehiculo: Vehiculo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${vehiculo.marca} - ${vehiculo.carroceria}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Color: ${vehiculo.color}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Precio: \$${vehiculo.valorDia}/día", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
