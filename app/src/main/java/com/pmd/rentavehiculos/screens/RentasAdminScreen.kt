package com.pmd.rentavehiculos.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.modelo.Vehiculo
import com.pmd.rentavehiculos.viewModels.AdminViewModel
import com.pmd.rentavehiculos.viewModels.VehiculosViewModel

@Composable
fun RentasAdminScreen(
    apiKey: String,
    vehiculoId: Int,
    adminViewModel: AdminViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    // Llama a la función para obtener el historial de rentas del vehículo
    LaunchedEffect(key1 = vehiculoId) {
        adminViewModel.obtenerHistorialRentas(apiKey, vehiculoId)
    }

    // Observa el LiveData que contiene el historial de rentas
    val rentas by adminViewModel.rentasLiveData.observeAsState(emptyList())
    val errorMessage by adminViewModel.errorLiveData.observeAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo que ocupa toda la pantalla
        Image(
            painter = painterResource(id = R.drawable.rentaf),
            contentDescription = "Fondo del menú de vehículos rentados",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Contenedor del contenido de la pantalla
        Column(
            modifier = Modifier
                .align(Alignment.Center) // Se centra en el Box
                .wrapContentHeight()     // La altura se ajusta al contenido
                .fillMaxWidth()
                .offset(y = 100.dp)
                .padding(horizontal = 32.dp, vertical = 48.dp)
                .background(Color.White.copy(alpha = 0.90f), shape = RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            // Fila para el botón de volver
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(55.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        modifier = Modifier.size(50.dp)
                    )
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(end = 20.dp)
                    ) {
                        Text(
                            text = "Historial",
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 40.sp),
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "de Rentas",
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 40.sp),
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFFFC107),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Contenido de la pantalla
            when {
                errorMessage != null -> {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                rentas.isEmpty() -> {
                    Text(
                        text = "No hay vehículos rentados",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                else -> {
                    LazyColumn(modifier = Modifier.weight(1f)) { // PARA QUE OCUPE TODO EL ESPACIO Y PODER HACER SCROLL
                        items(rentas) { renta ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.90f)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    AsyncImage(
                                        model = renta.vehiculo.imagen, // URL de la imagen
                                        contentDescription = "Imagen de ${renta.vehiculo.marca} ${renta.vehiculo.id}",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.height(15.dp))
                                    Text(
                                        text = buildAnnotatedString {
                                            append("Rentado por: ")
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                append("${renta.persona.nombre} ${renta.persona.apellidos}")
                                            }
                                        },
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        text = buildAnnotatedString {
                                            append("Identificación: ")
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                append("${renta.persona.tipo_identificacion} ${renta.persona.identificacion}")
                                            }
                                        },
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        text = buildAnnotatedString {
                                            append("Dirección: ")
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                append(renta.persona.direccion)
                                            }
                                        },
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        text = buildAnnotatedString {
                                            append("Teléfono: ")
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                append(renta.persona.telefono)
                                            }
                                        },
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(15.dp))
                                    Text(
                                        text = buildAnnotatedString {
                                            append("Días alquilado: ")
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                                                append(renta.dias.toString())
                                            }
                                        },
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center,
                                        color = Color(0xFFFFC107),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Text(
                                        text = buildAnnotatedString {
                                            append("Fecha de Renta: ")
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                append(renta.fechaRenta)
                                            }
                                        },
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = buildAnnotatedString {
                                            append("Fecha Prevista Entrega: ")
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                append(renta.fechaPrevistaEntrega)
                                            }
                                        },
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = buildAnnotatedString {
                                            append("Fecha Entrega: ")
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                                                append(renta.fechaEntrega ?: "Pendiente")
                                            }
                                        },
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Text(
                                        text = buildAnnotatedString {
                                            append("Valor por día: ")
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                append(renta.vehiculo.valor_dia.toString())
                                            }
                                        },
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = buildAnnotatedString {
                                            append("Valor Total: ")
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                                                append(renta.valorTotal.toString())
                                            }
                                        },
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center,
                                        color = Color(0xFFFFC107),
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}