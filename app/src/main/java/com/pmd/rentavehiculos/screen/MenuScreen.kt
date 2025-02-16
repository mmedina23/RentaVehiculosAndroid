package com.pmd.rentavehiculos.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil del Usuario", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0077B7)),
                modifier = Modifier.height(38.dp)
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
            // Logo Principal
            Image(
                painter = painterResource(id = R.drawable.logo2),
                contentDescription = "Logo principal",
                modifier = Modifier
                    .width(300.dp)
                    .height(350.dp)
            )

            Text(
                text = "Bienvenido",
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif,
                color = Color(0xFF0077B7),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Botones principales
            Button(
                onClick = { navController.navigate("vehiculos") },
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0077B7),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("🚗 Alquilar un Vehículo", fontSize = 16.sp)
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
                Text("📋 Mis Vehículos Alquilados", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🔥 Nueva Sección: Beneficios del servicio
            Text(
                text = "💡 ¿Por qué alquilar con nosotros?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0077B7),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listOf("🚘 Amplia flota", "⏳ Alquiler flexible", "\uD83D\uDCB5 Precios bajos", "\uD83C\uDF0D Cobertura Internacional")) { beneficio ->
                    BeneficioCard(beneficio)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = Color.Gray, thickness = 2.dp, modifier = Modifier.width(500.dp))
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "📞 Atención al Cliente: 91-688-00-01",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "\uD83C\uDF10  www.alexrenting.com",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Perfil de Usuario", fontWeight = FontWeight.ExtraBold)
                    Spacer(modifier = Modifier.padding(3.dp))
                    HorizontalDivider(color = Color.Gray, thickness = 2.dp, modifier = Modifier.width(280.dp))
                    Spacer(modifier = Modifier.padding(3.dp))
                }
            },
            text = {
                usuario?.let {
                    Column {
                        Text("\uD83D\uDC64 Nombre: ${it.nombre} ${it.apellidos}", fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("\uD83C\uDD94 Identificación: ${it.tipo_identificacion} ${it.identificacion}", fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("\uD83D\uDCDE Teléfono: ${it.telefono}", fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("\uD83D\uDCCD Localidad: ${it.direccion}", fontWeight = FontWeight.SemiBold)
                    }
                } ?: Text("No hay usuario logueado")
            },
            confirmButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0077B7))
                ) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
fun BeneficioCard(beneficio: String) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(80.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4B400 ))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = beneficio,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

