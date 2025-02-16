package com.pmd.rentavehiculos.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.pmd.rentavehiculos.modelo.Vehiculo
import com.pmd.rentavehiculos.viewModels.VehiculosViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.data.SessionManager
import com.pmd.rentavehiculos.modelo.PersonaRequest
import com.pmd.rentavehiculos.modelo.RentarVehiculoRequest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Composable
fun DetalleVehiculoScreen(
    vehiculo: Vehiculo,
    apiKey: String,
    personaId: Int,
    onRentSuccess: () -> Unit,
    onBack: () -> Unit,
    sessionManager: SessionManager,
    isAdmin: Boolean = false, // Indica si es admin
    vehiculosViewModel: VehiculosViewModel = viewModel()
) {
    var diasRenta by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    val errorMessage by vehiculosViewModel.errorLiveData.observeAsState()

    // Convertir el texto a entero y calcular el total
    val diasInt = diasRenta.toIntOrNull() ?: 0
    val totalRenta = diasInt * vehiculo.valor_dia

    // Usamos un Box para colocar el fondo y el contenido encima
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo que ocupa toda la pantalla
        Image(
            painter = painterResource(id = R.drawable.detalles2),
            contentDescription = "Fondo de Detalle del Vehículo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Contenedor del contenido de la pantalla
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 50.dp)
                .fillMaxWidth()            // Ocupa todo el ancho disponible
                .wrapContentHeight()       // La altura se ajusta al contenido
                .padding(16.dp)
                .background(Color.White.copy(alpha = 0.80f), shape = RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            AsyncImage(
                model = vehiculo.imagen,
                contentDescription = "Imagen de ${vehiculo.marca}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()  // Ocupa todo el ancho disponible
                    .height(200.dp)  // Altura fija de 200 dp
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = buildAnnotatedString {
                    append("Marca: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(vehiculo.marca)
                    }
                },
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = buildAnnotatedString {
                    append("Color: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(vehiculo.color)
                    }
                },
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = buildAnnotatedString {
                    append("Carrocería: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(vehiculo.carroceria)
                    }
                },
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = buildAnnotatedString {
                    append("Plazas: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(vehiculo.plazas.toString())
                    }
                },
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = buildAnnotatedString {
                    append("Cambios: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(vehiculo.cambios)
                    }
                },
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = buildAnnotatedString {
                    append("Tipo de combustible: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(vehiculo.tipo_combustible)
                    }
                },
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = buildAnnotatedString {
                    append("Alquiler por día: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(vehiculo.valor_dia.toString())
                    }
                },
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Si es admin, muestra botones para eliminar o modificar.
            if (isAdmin) {
                /*  Button(
                    onClick = { /* Acción extra para admin, por ejemplo editar */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Editar Vehículo")
                }
                Spacer(modifier = Modifier.height(8.dp))
                //En el futuro, se podría habilitar la eliminación del vehículo.
                  Button(
                    onClick = {
                        // Lógica para eliminar el vehículo
                        // vehiculosViewModel.eliminarVehiculo(apiKey, vehiculo.id)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Eliminar Vehículo")
                }
                Spacer(modifier = Modifier.height(8.dp))
                */
                // Nota: Para el admin, no se permite rentar vehículos.
                Text("Los administradores no pueden rentar vehículos", style = MaterialTheme.typography.bodyMedium)
            } else {
                // Para clientes: Mostrar el formulario de renta.
                OutlinedTextField(
                    value = diasRenta,
                    onValueChange = { diasRenta = it },
                    label = { Text("Días de Renta") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (diasInt > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Total a pagar: $totalRenta", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.ExtraBold)
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (errorMessage != null) {
                    Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
                }

                Button(
                    onClick = {
                        val dias = diasRenta.toIntOrNull()
                        if (dias == null || dias <= 0) {
                            vehiculosViewModel.errorLiveData.value = "Ingrese una cantidad válida de días"
                            return@Button
                        }

                        // Obtén los datos reales del usuario desde el SessionManager
                        val persona = sessionManager.persona
                        if (persona == null) {
                            vehiculosViewModel.errorLiveData.value = "No se encontró información del usuario."
                            return@Button
                        }

                        // Crea el objeto PersonaRequest incluyendo solo el id
                        val personaRequest = PersonaRequest(id = persona.id.toString())

                        // Calcula el total y formatea las fechas
                        val valorTotal = (vehiculo.valor_dia * dias).toInt()
                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
                            timeZone = TimeZone.getTimeZone("UTC")
                        }
                        val calendar = Calendar.getInstance()
                        val fechaRenta = sdf.format(calendar.time)
                        calendar.add(Calendar.DAY_OF_YEAR, dias)
                        val fechaEstimadaEntrega = sdf.format(calendar.time)
                        Log.d("FECHA", fechaEstimadaEntrega)

                        // Crea el objeto RentarVehiculoRequest sin incluir información del vehículo
                        val request = RentarVehiculoRequest(
                            persona = personaRequest,
                            dias_renta = dias,
                            valor_total_renta = valorTotal,
                            fecha_renta = fechaRenta,
                            fecha_estimada_entrega = fechaEstimadaEntrega
                        )

                        // Imprime el JSON generado para depurar
                        val jsonRequest = Gson().toJson(request)
                        Log.d("REQUEST_JSON", jsonRequest)

                        isProcessing = true
                        vehiculosViewModel.rentarVehiculo(
                            apiKey = apiKey,
                            usuario = persona,
                            vehiculoId = vehiculo.id,
                            request = request,
                            onSuccess = {
                                isProcessing = false
                                onRentSuccess()
                            },
                            onError = { mensaje ->
                                isProcessing = false
                                // si hay error finaliza el proceso
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isProcessing
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Text("Rentar Vehículo", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Regresar", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            }
        }
    }
}
