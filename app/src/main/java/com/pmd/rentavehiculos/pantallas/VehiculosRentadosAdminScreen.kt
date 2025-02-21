package com.pmd.rentavehiculos.pantallas

// Importación de librerías necesarias para la interfaz y la gestión de estados.
import android.graphics.Color.rgb
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.ClasesPrincipales.RentaRequest
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.viewmodel.LoginViewModel
import com.pmd.rentavehiculos.viewmodel.VehiculosViewModel

@Composable
fun VehiculosRentadosAdminScreen(
    navController: NavController,
    vehiculoId: Int,
    vehiculosViewModel: VehiculosViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel()
) {
    val apiKey = loginViewModel.apiKey.value
    val rentas = vehiculosViewModel.rentas

    LaunchedEffect(apiKey, vehiculoId) {
        if (apiKey != null) {
            vehiculosViewModel.obtenerListadoVehiculosAdmin(apiKey, vehiculoId)
        }
    }

    // Contenedor principal
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.logo4), // Asegúrate de que el archivo esté en res/drawable
            contentDescription = "Fondo",
            modifier = Modifier.matchParentSize(), // Hace que la imagen llene todo el espacio
            contentScale = ContentScale.Crop // Ajusta la imagen sin distorsionarla
        )

        // Capa de fondo semitransparente para mejorar la visibilidad del texto
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)) // Fondo negro con opacidad del 40%
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Título
            Text(
                text = "Historial de Rentas",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Historial del vehículo ID: $vehiculoId",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // Si no hay rentas, muestra un mensaje
            if (rentas.isEmpty()) {
                Text(
                    text = "No hay historial de rentas para este vehículo.",
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                // Lista de rentas del vehículo
                LazyColumn {
                    items(rentas) { renta ->
                        RentaCardAdmin(renta)
                    }
                }
            }
        }
    }
}

@Composable
fun RentaCardAdmin(renta: RentaRequest) {
    // Card con fondo amarillo y bordes redondeados.
    Card(
        modifier = Modifier
            .fillMaxWidth() // Ocupa todo el ancho disponible.
            .padding(vertical = 8.dp), // Espacio entre tarjetas.
        colors = CardDefaults.cardColors(
            containerColor = Color(rgb(251, 191, 36))
        ),
        shape = RoundedCornerShape(12.dp), // Bordes redondeados
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp) // Sombra ligera
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Marca y color del vehículo en texto destacado.
            Text(
                text = " ${renta.vehiculo.marca} - ${renta.vehiculo.color}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black // Texto en negro para contrastar con el amarillo
            )
            Spacer(modifier = Modifier.height(8.dp)) // Espaciado entre elementos.

            // Detalles de la renta
            Text(text = " Cliente: ${renta.persona.nombre} ${renta.persona.apellidos}", color = Color.DarkGray)
            Text(text = " Días rentados: ${renta.dias_renta}", color = Color.DarkGray)
            Text(text = " Valor total: $${renta.valor_total_renta}", color = Color.DarkGray)
            Text(text = " Fecha de renta: ${renta.fecha_renta}", color = Color.DarkGray)
            Text(text = " Fecha estimada de entrega: ${renta.fecha_estimada_entrega}", color = Color.DarkGray)

            // Estado de entrega en color rojo o verde
            Text(
                text = if (!renta.fecha_entrega.isNullOrEmpty()) {
                    " Entregado el: ${renta.fecha_entrega}"
                } else {
                    " Aún no ha sido entregado..."
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (!renta.fecha_entrega.isNullOrEmpty()) Color.Green else Color.Red
            )
        }
    }
}
