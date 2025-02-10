package com.pmd.rentavehiculos.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.vista.VistaLogin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuAdminScreen(navController: NavController, loginViewModel: VistaLogin = viewModel()) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var vehiculoId by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Panel de Administrador", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0D77A1),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.npc),
                contentDescription = "Imagen de Administrador",
                modifier = Modifier
                    .size(300.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = "Bienvenido, Administrador",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Button(
                onClick = { navController.navigate("vehiculos_disponibles") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0077B7)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text("Gestión de Vehículos", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { isDialogOpen = true },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0077B7)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ver Historial de Rentas", fontSize = 16.sp)
            }

            if (isDialogOpen) {
                AlertDialog(
                    onDismissRequest = { isDialogOpen = false },
                    title = { Text("Buscar vehiculo") },
                    text = {
                        Column {
                            Text("ID del vehículo:")
                            OutlinedTextField(
                                value = vehiculoId,
                                onValueChange = { vehiculoId = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                isDialogOpen = false
                                if (vehiculoId.isNotEmpty()) {
                                    navController.navigate("vehiculos_rentados_admin/$vehiculoId")
                                }
                            },

                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0077B7),
                                contentColor = Color.White
                        ) ){
                            Text("Buscar")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { isDialogOpen = false },

                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0077B7),
                                contentColor = Color.White)) {
                            Text("Cancelar")
                        }
                    },
                    containerColor = Color.White

                )
            }
        }
    }
}

