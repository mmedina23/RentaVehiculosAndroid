package com.pmd.rentavehiculos.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmd.rentavehiculos.viewModels.AdminViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.modelo.Vehiculo

@Composable
fun RentaVehiculosListScreen(
    apiKey: String,
    adminViewModel: AdminViewModel = viewModel(),
    onVehicleClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    // Llama a la función para obtener vehículos rentados al iniciar
    LaunchedEffect(Unit) {
        adminViewModel.obtenerVehiculosRentadosAdmin(apiKey)
    }

    // Observa el LiveData que contendrá la lista de vehículos rentados
    val vehiculosRentados by adminViewModel.vehiculosRentadosAdminLiveData.observeAsState(emptyList())
    val errorMessage by adminViewModel.errorLiveData.observeAsState()

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
                .align(Alignment.Center)
                .offset(y = 200.dp)
                .wrapContentHeight() // La altura se ajusta al contenido
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
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(55.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver atrás",
                        modifier = Modifier.size(50.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                // Usamos una Column para dividir "Vehículos" y "Disponibles" en dos líneas
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
                vehiculosRentados.isEmpty() -> {
                    // Mientras se carga, muestra un indicador
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                else -> {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(vehiculosRentados) { vehiculo ->
                            VehiculoItem(vehiculo = vehiculo) {
                                onVehicleClick(vehiculo.id)
                            }
                        }
                    }
                }
            }
        }
    }
}
