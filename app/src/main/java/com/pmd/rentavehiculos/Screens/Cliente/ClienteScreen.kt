package com.pmd.rentavehiculos.Screens.Cliente

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.pmd.rentavehiculos.Api.RetrofitInstance
import com.pmd.rentavehiculos.Entity.Logout
import com.pmd.rentavehiculos.Entity.PersonaRequestRenta
import com.pmd.rentavehiculos.Entity.Usuario
import com.pmd.rentavehiculos.Entity.Vehiculo
import com.pmd.rentavehiculos.Entity.VehiculoRequestRenta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClienteScreen(
    token: String,
    personaId: Int,
    Persona : PersonaRequestRenta,
    NavigationRentarVehiculo:(personaId : Int,
                              vehiculoId : Int,
                              token : String,
                              precioDiaVehiculo : Double, Persona : PersonaRequestRenta,
                              Vehiculo : VehiculoRequestRenta)->Unit,
    NavigationMisVehiculos:(token: String, personaId : Int) -> Unit,
    NavigationLogin: () -> Unit
    )
{
    val service = RetrofitInstance.makeRetrofitService()
    var vehiculos by remember { mutableStateOf<List<Vehiculo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var filtroSeleccionado by remember { mutableStateOf("Todos") }
    val coroutineScope = rememberCoroutineScope()
    var expandedVehiculo by remember { mutableStateOf<Vehiculo?>(null) } // Vehículo seleccionado

    val filtros = listOf("Todos", "Disponibles", "No Disponibles")
    var expanded by remember { mutableStateOf(false) }


    fun cargarVehiculos(filtro: String) {
        coroutineScope.launch {
            isLoading = true
            try {
                val filtroApi = when (filtro) {
                    "Disponibles" -> "disponibles"
                    "No Disponibles" -> "rentados"
                    else -> null
                }
                vehiculos = service.obtenervehiculos(token, filtroApi)
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
            }
            isLoading = false
        }
    }

    LaunchedEffect(Unit) { cargarVehiculos(filtroSeleccionado) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /*Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { NavigationCarList(token) }) {
                Text(text = "Vehículos Disponibles")
            }
            Button(onClick = {}) {
                Text(text = "Vehículos Rentados")
            }
        }*/
        
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {NavigationMisVehiculos(token, personaId)}) {
            Text(text = "Mis vehiculos")
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        println("personaid $personaId")
        println("personarequestId ${Persona.personaId}")
        Button(onClick = {
            GlobalScope.launch {
            verificarLogout(token, Persona.personaId, NavigationLogin)}
        }) {
            Text(text = "Cerrar sesión")
        }

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = filtroSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Filtrar por") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .clickable { expanded = true }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                filtros.forEach { filtro ->
                    DropdownMenuItem(
                        text = { Text(filtro) },
                        onClick = {
                            filtroSeleccionado = filtro
                            expanded = false
                            cargarVehiculos(filtro)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyLarge)
            }

            if (vehiculos.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp)
                ) {
                    items(vehiculos.size) { index ->
                        VehiculoItem(vehiculos[index]) {
                            expandedVehiculo = it
                        }
                    }
                }
            } else {
                Text("No hay vehículos disponibles.", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }

    // Mostrar el cuadro expandido si hay un vehículo seleccionado
    expandedVehiculo?.let { vehiculo ->
        DetalleVehiculoDialog(vehiculo, token, personaId, Persona,  NavigationRentarVehiculo) {
            expandedVehiculo = null
        }
    }
}

@Composable
fun VehiculoItem(vehiculo: Vehiculo, onClick: (Vehiculo) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(vehiculo) }, // Al hacer clic, muestra los detalles
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Vehículo: ${vehiculo.id}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF37474F)
            )
            Spacer(modifier = Modifier.height(8.dp))

            VehiculoInfoItem(label = "Marca", value = vehiculo.marca)
            VehiculoInfoItem(label = "Disponible", value = if (vehiculo.disponible) "Sí" else "No")
        }
    }
}

@Composable
fun DetalleVehiculoDialog(vehiculo: Vehiculo, token: String, personaId: Int, Persona: PersonaRequestRenta, NavigationRentarVehiculo: (personaId: Int, vehiculoId: Int, token : String, precioDiaVehiculo : Double, Persona : PersonaRequestRenta, Vehiculo : VehiculoRequestRenta) -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cerrar")
            }
        },
        title = { Text("Detalles del Vehículo") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberAsyncImagePainter(vehiculo.imagen),
                    contentDescription = "Imagen del vehículo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                VehiculoInfoItem(label = "Color", value = vehiculo.color)
                VehiculoInfoItem(label = "Carrocería", value = vehiculo.carroceria)
                VehiculoInfoItem(label = "Cambios", value = vehiculo.cambios)
                VehiculoInfoItem(label = "Marca", value = vehiculo.marca)
                VehiculoInfoItem(label = "Plazas", value = vehiculo.plazas.toString())
                VehiculoInfoItem(label = "Disponible", value = if (vehiculo.disponible) "Sí" else "No")
                VehiculoInfoItem(label = "Combustible", value = vehiculo.tipoCombustible)
                Spacer(modifier = Modifier.height(10.dp))
                val vehiculoId = vehiculo.id
                val precioVehiculo = vehiculo.precioDia?.toDouble()?:0.0
                VehiculoInfoItem(label = "valor Renta", value = precioVehiculo.toString())
                val Vehiculo = VehiculoRequestRenta(
                    marca = vehiculo?.marca?:"",
                    color = vehiculo?.color?:"",
                    carroceria = vehiculo?.carroceria?:"",
                    plazas = vehiculo?.plazas?:0,
                    cambios = vehiculo?.cambios?:"",
                    tipoCombustible = vehiculo?.tipoCombustible?:"",
                    valorDia = vehiculo?.precioDia?.toDouble()?:0.0,
                    disponible = vehiculo?.disponible?:false,
                    imagen = vehiculo?.imagen?:""
                    )
                Button(onClick = {NavigationRentarVehiculo(personaId, vehiculoId, token, precioVehiculo,Persona, Vehiculo)}) {
                    Text(text = "Rentar")
                }
            }
        }
    )
}


@Composable
fun VehiculoInfoItem(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$label: ", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}


suspend fun verificarLogout(token : String, personaId : Int, NavigationLogin:() -> Unit){
    println(token)
    println(personaId)
    val LogoutBody = Logout(idUsuario = personaId, token = token)
    try {
        val service = RetrofitInstance.makeRetrofitService()
        val response = service.logout(LogoutBody)

        if(response.isSuccessful){
            withContext(Dispatchers.Main) {  // Asegurar que la navegación ocurre en el hilo principal
                NavigationLogin()
            }
        }else{
            println("error a la hora de hacer el logout")
        }
    }catch (e : Exception){
        println(e.message)
    }
}