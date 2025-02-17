package com.pmd.rentavehiculos.Screens.Cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pmd.rentavehiculos.Api.RetrofitInstance
import com.pmd.rentavehiculos.Entity.PersonaRequestRenta
import com.pmd.rentavehiculos.Entity.RentaRequest
import com.pmd.rentavehiculos.Entity.UserRequest
import com.pmd.rentavehiculos.Entity.VehiculoRequestRenta
import com.pmd.rentavehiculos.Screens.SanFrancisco
import com.pmd.rentavehiculos.Screens.mostrarAlerta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun RentarVehiculoFunction(personaId: Int,
                           vehiculoId: Int,
                           token: String,
                           precioDiaVehiculo : Double,
                           Persona : PersonaRequestRenta,
                           Vehiculo : VehiculoRequestRenta
                           ) {
    // Estados para almacenar la selección del usuario
    var selectedYear by remember { mutableStateOf("2025") }
    var selectedMonth by remember { mutableStateOf("01") }
    var selectedDay by remember { mutableStateOf("01") }
    var diasRentados by remember { mutableStateOf(1) } // Nuevo estado para los días rentados
    var fechaGenerada by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Selecciona una fecha", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Selector de Año
        DropdownSelector(
            label = "Año",
            options = (2000..2025).map { it.toString() },
            selectedValue = selectedYear,
            onValueChange = { selectedYear = it }
        )

        // Selector de Mes
        DropdownSelector(
            label = "Mes",
            options = (1..12).map { it.toString().padStart(2, '0') },
            selectedValue = selectedMonth,
            onValueChange = { selectedMonth = it }
        )

        // Selector de Día
        DropdownSelector(
            label = "Día",
            options = (1..29).map { it.toString().padStart(2, '0') },
            selectedValue = selectedDay,
            onValueChange = { selectedDay = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Nuevo selector de días rentados
        Text(text = "Elige los días que lo rentarás", fontWeight = FontWeight.Bold)

        DropdownSelector(
            label = "Días",
            options = (1..10).map { it.toString() },
            selectedValue = diasRentados.toString(),
            onValueChange = { diasRentados = it.toInt() }
        )

        Spacer(modifier = Modifier.height(20.dp))

        val Persona = Persona
        val Vehiculo = Vehiculo

        Spacer(modifier = Modifier.height(20.dp))

        val fechaEstimadaEntrega = "$selectedYear-$selectedMonth-${(selectedDay.toInt() + 1).toString()}" + "T21:45:41.647Z"

        Button(onClick = {rentarVehiculo(Vehiculo, Persona,vehiculoId, token, personaId, diasRentados  ,fechaGenerada, fechaEstimadaEntrega)}) {
        }
        // Botón para generar fecha
        Button(onClick = {
            fechaGenerada = "$selectedYear-$selectedMonth-$selectedDay" + "T21:45:41.647Z"
        }) {
            Text("Generar Fecha")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Mostrar la fecha generada y los días rentados
        if (fechaGenerada.isNotEmpty()) {
            Text(text = "Fecha Generada: $fechaGenerada", fontWeight = FontWeight.Bold)
            Text(text = "Días Rentados: $diasRentados", fontWeight = FontWeight.Bold)
        }
    }
}

fun rentarVehiculo(Vehiculo : VehiculoRequestRenta, Persona: PersonaRequestRenta, vehiculoId: Int, token: String, personaId: Int, diasRenta : Int  ,fechaGenerada : String, fechaEstimadaEntrega : String){
    val RentaRequest = RentaRequest(persona = Persona, vehiculo = Vehiculo, diasRenta = diasRenta.toDouble(), fechaRenta = fechaGenerada, fechaEstimadaEntrega = fechaEstimadaEntrega)
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val service = RetrofitInstance.makeRetrofitService()
            val response = service.rentarVehiculo(vehiculoId = vehiculoId, apiKey = token, RentaRequest)
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                println(e)
            }
        }
    }
}


@Composable
fun DropdownSelector(label: String, options: List<String>, selectedValue: String, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = label, fontWeight = FontWeight.SemiBold)

        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(vertical = 8.dp)) {
            Text(text = selectedValue, modifier = Modifier.padding(8.dp))
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}