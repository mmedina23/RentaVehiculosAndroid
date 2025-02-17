package com.pmd.rentavehiculos.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.viewmodel.LoginViewModel


@Composable
fun PantallaInicioAdmi(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var vehiculoId by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        // Imagen de fondo con efecto de oscurecimiento
        Image(
            painter = painterResource(id = R.drawable.logo4),
            contentDescription = "Fondo de administrador",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Administrador iniciado",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            AdminButton(text = "Gestión de Vehículos") {
                navController.navigate("vehiculos_disponibles")
            }

            Spacer(modifier = Modifier.height(12.dp))

            AdminButton(text = "Ver Historial de Rentas") {
                isDialogOpen = true
            }

            if (isDialogOpen) {
                HistorialRentasDialog(
                    vehiculoId = vehiculoId,
                    onVehiculoIdChange = { vehiculoId = it },
                    onDismiss = { isDialogOpen = false },
                    onConfirm = {
                        if (vehiculoId.isNotEmpty()) {
                            navController.navigate("vehiculos_rentados_admin/$vehiculoId")
                        }
                        isDialogOpen = false
                    }
                )
            }
        }
    }
}


@Composable
fun AdminButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(251, 191, 36)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text, fontSize = 16.sp)
    }
}

@Composable
fun HistorialRentasDialog(
    vehiculoId: String,
    onVehiculoIdChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Buscar Historial de Rentas") },
        text = {
            Column {
                Text("Ingrese el ID del vehículo:")
                OutlinedTextField(
                    value = vehiculoId,
                    onValueChange = onVehiculoIdChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Buscar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
/*
@Preview(showBackground = true)
@Composable
fun PreviewPantallaInicioAdmi() {
    MaterialTheme {
        Column {
            Text("Vista previa no disponible para NavController.")
        }
    }
}
*/