package com.pmd.rentavehiculos.views

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.pmd.rentavehiculos.models.Vehiculo
import com.pmd.rentavehiculos.models.VehiculoRentado
import com.pmd.rentavehiculos.network.ApiClient
import com.pmd.rentavehiculos.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token = intent.getStringExtra("TOKEN") ?: ""
        val personaId = intent.getIntExtra("PERSONA_ID", 0) // ✅ Obtener personaId correctamente

        setContent {
            AdminScreen(token, personaId) // ✅ Ahora se pasa personaId
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun AdminScreen(token: String, personaId: Int) { // ✅ Se pasa personaId correctamente
    var tabIndex by remember { mutableStateOf(0) }
    val context = LocalContext.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("Administrador") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(selectedTabIndex = tabIndex) {
                Tab(selected = tabIndex == 0, onClick = { tabIndex = 0 }, text = { Text("Disponibles") })
                Tab(selected = tabIndex == 1, onClick = { tabIndex = 1 }, text = { Text("Rentados") })
            }

            when (tabIndex) {
                0 -> VehiculosDisponiblesScreen(token)
                1 -> VehiculosRentadosScreen(token, personaId) // ✅ Se pasa personaId
            }
        }
    }
}

// 🔹 PANTALLA DE VEHÍCULOS DISPONIBLES
@Composable
fun VehiculosDisponiblesScreen(token: String) {
    val context = LocalContext.current
    var vehiculos by remember { mutableStateOf<List<Vehiculo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val apiService = ApiClient.retrofit.create(ApiService::class.java)

    LaunchedEffect(Unit) {
        val call = apiService.obtenerVehiculos(token)
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

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn {
            items(vehiculos) { vehiculo ->
                VehiculoAdminItem(vehiculo)
            }
        }
    }
}

// 🔹 PANTALLA DE VEHÍCULOS RENTADOS
@Composable
fun VehiculosRentadosScreen(token: String, personaId: Int) { // ✅ Se recibe personaId
    val context = LocalContext.current
    var vehiculosRentados by remember { mutableStateOf<List<VehiculoRentado>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val apiService = ApiClient.retrofit.create(ApiService::class.java)

    LaunchedEffect(Unit) {
        val call = apiService.obtenerVehiculosRentados(token, personaId) // ✅ Ahora se pasa personaId
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

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn {
            items(vehiculosRentados) { vehiculoRentado ->
                VehiculoRentadoItem(vehiculoRentado)
            }
        }
    }
}



// 🔹 ITEM DE VEHÍCULO RENTADO
@Composable
fun VehiculoRentadoItem(vehiculoRentado: VehiculoRentado) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "ID: ${vehiculoRentado.id} - ${vehiculoRentado.marca}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Días rentados: ${vehiculoRentado.diasRentados}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Total: \$${vehiculoRentado.precioTotal}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
