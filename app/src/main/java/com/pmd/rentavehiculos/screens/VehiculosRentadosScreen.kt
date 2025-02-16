package com.pmd.rentavehiculos.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
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
import com.pmd.rentavehiculos.modelo.RentaVehiculo
import com.pmd.rentavehiculos.viewModels.VehiculosRentadosViewModel

@Composable
fun VehiculosRentadosScreen(
    apiKey: String,
    personaId: Int,
    onEntregarVehiculo: (Int) -> Unit,
    onBack: () -> Unit,
    rentadosViewModel: VehiculosRentadosViewModel = viewModel()
) {
    // Se consulta la API una sola vez cuando se carga la pantalla
    LaunchedEffect(Unit) {
        rentadosViewModel.obtenerVehiculosRentados(apiKey, personaId)
    }

    // Especifico el tipo vacío para evitar inferencias erróneas
    val rentas by rentadosViewModel.vehiculosRentadosLiveData.observeAsState(emptyList<RentaVehiculo>())
    val errorMessage by rentadosViewModel.errorLiveData.observeAsState()

    // Uso un Box para colocar el fondo y el contenido encima
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo que ocupa toda la pantalla
        Image(
            painter = painterResource(id = R.drawable.rentlist),
            contentDescription = "Fondo del menú de vehículos rentados",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Contenedor del contenido de la pantalla
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .align(Alignment.Center) // Se centra en el Box
                .wrapContentHeight()     // La altura se ajusta al contenido
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 48.dp)
                .background(Color.White.copy(alpha = 0.90f), shape = RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            // Encabezado con botón de volver atrás
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                // Uso una Column para dividir "Vehículos" y "Rentados" en dos líneas
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp)
                ) {
                    Text(
                        text = "Vehículos",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 40.sp),
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Rentados",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 40.sp),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFFFC107),
                        textAlign = TextAlign.Center
                    )
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
                    // Mientras se carga o si la lista está vacía, muestra un indicador
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                else -> {
                    LazyColumn(modifier = Modifier.weight(1f)) { // modifier = Modifier.weight(1f) PARA QUE OCUPE TODO EL ESPACIO Y PODER HACER SCROLL
                        items(rentas) { renta ->
                            RentadoItem(renta = renta, onEntregar = {
                                // Llamada a la función del view model para entregar el vehículo
                                rentadosViewModel.entregarVehiculo(apiKey, renta.vehiculo.id)
                                // Refrescar la lista luego de entregar
                                rentadosViewModel.obtenerVehiculosRentados(apiKey, personaId)
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RentadoItem(renta: RentaVehiculo, onEntregar: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.90f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = renta.vehiculo.imagen,
                contentDescription = "Imagen de ${renta.vehiculo.marca}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()  // Ocupa todo el ancho disponible
                    .height(200.dp)  // Altura fija de 200 dp
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = buildAnnotatedString {
                    append("Vehículo: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(renta.vehiculo.marca)
                    }
                },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = buildAnnotatedString {
                    append("Color: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(renta.vehiculo.color)
                    }
                },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = buildAnnotatedString {
                    append("Carrocería: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(renta.vehiculo.carroceria)
                    }
                },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = buildAnnotatedString {
                    append("Plazas: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(renta.vehiculo.plazas.toString())
                    }
                },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = buildAnnotatedString {
                    append("Cambios: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(renta.vehiculo.cambios)
                    }
                },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = buildAnnotatedString {
                    append("Tipo de Combustible: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(renta.vehiculo.tipo_combustible)
                    }
                },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = buildAnnotatedString {
                    append("Alquiler por Día: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(renta.vehiculo.valor_dia.toString())
                    }
                },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = buildAnnotatedString {
                    append("Días de Renta: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(renta.dias.toString())
                    }
                },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
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
            Text(
                text = buildAnnotatedString {
                    append("Fecha Estimada Entrega: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(renta.fechaPrevistaEntrega)
                    }
                },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = buildAnnotatedString {
                    append("Valor Total: ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(renta.valorTotal.toString())
                    }
                },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            if (renta.fechaEntrega == null) {
                Button(
                    onClick = onEntregar,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Entregar Vehículo")
                }
            } else {
                Text("Vehículo entregado", modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}
