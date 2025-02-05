package com.pmd.rentavehiculos.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Pongo `LoginViewModel` desde MainActivity para que  las pantallas use la misma instancia Y EL USUARIO NO SEA NULL


fun MenuScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    val usuario = loginViewModel.usuario.value



    Log.d("MenuScreen","Usuario : $usuario")
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil del Usuario", tint = Color.Black)
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0077B7))
                       , modifier = Modifier.height(38.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo2),
                contentDescription = "Logo principal",
                modifier = Modifier
                    .width(300.dp)
                    .height(410.dp)
            )

            Text(
                text = "Bienvenido",
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif,
                color = Color(0xFF0077B7),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = { navController.navigate("vehiculos") },
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0077B7),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Alquilar un Vehículo", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.navigate("vehiculos_rentados") },
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0077B7),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Mis Vehículos Alquilados", fontSize = 16.sp)
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Column (horizontalAlignment = Alignment.CenterHorizontally){
                Text("Perfil de Usuario",
                    fontWeight = FontWeight.ExtraBold )
                        Spacer(modifier = Modifier.padding(3.dp))
                        HorizontalDivider(color = Color.Gray , thickness = 2.dp , modifier = Modifier.width(280.dp))
                        Spacer(modifier = Modifier.padding(3.dp))

                }

                    },
            text = {
                usuario?.let {
                    Column {
                        Text("\uD83D\uDC64 Nombre: ${it.nombre} ${it.apellidos}", fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("\uD83C\uDD94 Identificación: ${it.tipo_identificacion} ${it.identificacion}",fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("\uD83D\uDCDE Teléfono: ${it.telefono}",fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("\uD83D\uDCCD Dirección: ${it.direccion}",fontWeight = FontWeight.SemiBold)
                    }
                } ?: Text("No hay usuario logueado")
            },
            modifier = Modifier.background(color = Color.White),
            confirmButton = {
                Button(onClick = { showDialog = false },colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0077B7)),
                ) {
                    Text("Cerrar")
                }
            }
        )
    }
}

