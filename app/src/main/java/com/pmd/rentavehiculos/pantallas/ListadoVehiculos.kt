package com.pmd.rentavehiculos.pantallas

import android.graphics.Color.rgb
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.ClasesPrincipales.Vehiculo
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.viewmodel.LoginViewModel
import com.pmd.rentavehiculos.viewmodel.VehiculosViewModel
import kotlinx.coroutines.launch

@Composable
fun ListadoVehiculos(
    navController: NavController, // Controlador de navegación
    vehiculosViewModel: VehiculosViewModel = viewModel(), // ViewModel para manejar los datos de los vehículos
    loginViewModel: LoginViewModel = viewModel() // ViewModel para manejar la sesión de usuario
) {
    // Obtenemos la clave de API y el usuario desde los ViewModels
    val apiKey = loginViewModel.apiKey.value
    val usuario = loginViewModel.usuario.value
    val vehiculos = vehiculosViewModel.vehiculos // Lista de vehículos
    var selectedVehiculo by remember { mutableStateOf<Vehiculo?>(null) } // Vehículo seleccionado
    var showDialog by remember { mutableStateOf(false) } // Controla si se debe mostrar el cuadro de diálogo
    var diasRenta by remember { mutableStateOf("1") } // Número de días de renta
    val snackbarHostState = remember { SnackbarHostState() } // Estado para mostrar notificaciones de tipo snackbar

    /*
    Un Snackbar es un mensaje temporal que aparece en la parte inferior de la pantalla para proporcionar retroalimentación al usuario.
    Normalmente, los snackbars se usan para mostrar mensajes breves como notificaciones,confirmaciones, errores o advertencias.
    */

    val coroutineScope = rememberCoroutineScope() // Contexto para realizar tareas asíncronas

    // Cuando la API key cambie, obtenemos los vehículos
    LaunchedEffect(apiKey) {
        if (apiKey != null) {
            vehiculosViewModel.obtenerVehiculos(apiKey) // Llamada para obtener vehículos
        }
    }

    // Caja de contenido principal
    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.logo4),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Componente Scaffold que maneja el layout básico y las notificaciones
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }, // Muestra los mensajes de error o éxito
            containerColor = Color.Transparent // Fondo transparente
        ) { paddingValues ->
            // Columna que contiene los vehículos en una lista
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // LazyColumn para mostrar la lista de vehículos
                LazyColumn {
                    items(vehiculos) { vehiculo ->
                        // Tarjeta de vehículo
                        VehiculoCard(vehiculo) {
                            selectedVehiculo = vehiculo // Establece el vehículo seleccionado
                            showDialog = true // Muestra el cuadro de diálogo de reserva
                        }
                    }
                }
            }
        }
    }

    // Cuadro de diálogo para alquilar un vehículo
    if (showDialog && selectedVehiculo != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false }, // Cierra el cuadro de diálogo si se toca fuera de él
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Título
                    Text(
                        "Alquilar ${selectedVehiculo?.marca}",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(rgb(41, 37, 36))
                        )
                    )

                    // Información adicional debajo del título
                    Text(
                        "Color ${selectedVehiculo?.color}",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            color = Color(rgb(120, 120, 120)) // Color gris más suave
                        ),
                        modifier = Modifier.padding(top = 18.dp) // Espaciado entre el título y el texto adicional
                    )
                    Text(
                        "Numero de plazas ${selectedVehiculo?.plazas}",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            color = Color(rgb(120, 120, 120)) // Color gris más suave
                        ),
                        modifier = Modifier.padding(top = 2.dp) // Espaciado entre el título y el texto adicional
                    )
                    Text(
                        "Tipo de conducción  ${selectedVehiculo?.cambios}",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            color = Color(rgb(120, 120, 120)) // Color gris más suave
                        ),
                        modifier = Modifier.padding(top = 2.dp) // Espaciado entre el título y el texto adicional
                    )
                }
            },
            text = {
                Column {
                    // Instrucción de ingresar días de renta
                    Text(
                        ("Seleciona numero de días"),
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(rgb(41, 37, 36)) // Color gris más suave
                        ),
                        modifier = Modifier.padding(bottom = 10.dp) // Espaciado entre el título y el texto adicional
                        )
                    // Campo de texto para ingresar los días de renta
                    OutlinedTextField(
                        value = diasRenta,
                        onValueChange = { input ->
                            // Solo permite números en el campo
                            if (input.all { it.isDigit() }) {
                                diasRenta = input
                            }
                        },
                        label = { Text("Días de renta") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Cálculo y muestra del precio total
                    val dias = diasRenta.toIntOrNull() ?: 1 // Si no es válido, se toma como 1
                    val total = selectedVehiculo!!.valor_dia * dias // Cálculo del precio total
                    Text("Precio € ${total}")
                }
            },
            // Dentro de tu función confirmButton
            confirmButton = {
                // Botón para confirmar la reserva
                Button(
                    onClick = {
                        if (apiKey != null && usuario != null) {
                            val dias = diasRenta.toIntOrNull() ?: 1
                            // Realiza la reserva del vehículo
                            vehiculosViewModel.reservarVehiculo(
                                apiKey,
                                usuario,
                                selectedVehiculo!!,
                                dias
                            ) { success, message ->
                                // Muestra un mensaje en el snackbar
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message)
                                }
                                showDialog = false // Cierra el cuadro de diálogo
                            }
                        }
                    },
                    // Estilo de color usando la propiedad 'colors'
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(251, 191, 36),
                        contentColor = Color.White // Color del texto (blanco)
                    )
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                // Botón para cancelar la reserva
                Button(
                    onClick = { showDialog = false },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(251, 191, 36),// Cambiar a tu color deseado
                        contentColor = Color.White // Color del texto (blanco)
                    )
                ) {
                    Text("Cancelar")
                }
            }

        )
    }
}



@Composable
fun VehiculoCard(vehiculo: Vehiculo, onReservarClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth() // Ocupa todo el ancho disponible
            .padding(12.dp), // Agrega un margen exterior de 12dp
        colors = CardDefaults.cardColors(containerColor = Color(38, 38, 38, 128)) // Color gris oscuro semitransparente (128 = 50% opacidad)
    ) {
        Column(
            modifier = Modifier.padding(30.dp), // Aplica un relleno interno de 30dp
            horizontalAlignment = Alignment.CenterHorizontally // Centra los elementos horizontalmente dentro de la columna
        ) {
            // Contenedor de la imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth() // Hace que la imagen ocupe todo el ancho
                    .height(180.dp), // Define una altura fija de 180dp
                contentAlignment = Alignment.Center // Centra la imagen dentro del Box
            ) {
                AsyncImage(
                    model = vehiculo.imagen, // Carga la imagen del vehículo desde la URL o recurso
                    contentDescription = "Imagen del Vehículo", // Descripción accesible
                    modifier = Modifier.fillMaxSize() // Hace que la imagen llene el espacio disponible
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espaciado de 16dp entre la imagen y el texto

            Column(modifier = Modifier.fillMaxWidth()) { // Contenedor de la información del vehículo
                // Marca del vehículo
                Text(
                    text = vehiculo.marca,
                    textAlign = TextAlign.Center, // Centrar el texto
                    fontWeight = FontWeight.Bold, // Texto en negrita
                    fontSize = 26.sp, // Tamaño de la fuente
                    color = Color.White ,// Color blanco para el texto
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(30.dp)) // Espaciado de 10dp antes del precio

                // Precio del alquiler
                Text(
                    text = "Precio ${vehiculo.valor_dia} €",
                    fontWeight = FontWeight.ExtraBold, // Texto en negrita extra
                    fontSize = 20.sp, // Tamaño de la fuente
                    color = Color(251, 191, 36) // Color amarillo dorado
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espaciado de 16dp antes del botón

            // Botón para alquilar el vehículo
            Button(
                onClick = { onReservarClick() }, // Acción al hacer clic
                modifier = Modifier
                    .fillMaxWidth() // Hace que el botón ocupe todo el ancho
                    .height(50.dp), // Altura del botón de 50dp
                colors = ButtonDefaults.buttonColors(containerColor = Color(251, 191, 36)), // Color amarillo dorado
                shape = RoundedCornerShape(10.dp) // Bordes redondeados del botón
            ) {
                Spacer(modifier = Modifier.width(8.dp)) // Espaciado entre el ícono (si se agrega) y el texto
                Text(
                    "Alquilar", // Texto del botón
                    color = Color.Black, // Color del texto
                    fontFamily = FontFamily.SansSerif,//fuente del Texto
                    fontWeight = FontWeight.Bold, // Texto en negrita
                    fontSize = 18.sp // Tamaño de la fuente
                )
            }
        }
    }
}