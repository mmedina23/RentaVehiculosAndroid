package com.pmd.rentavehiculos.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.viewModels.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel()
) {
    loginViewModel.usuario.value  // Obtener usuario del ViewModel

    Scaffold(
        // Barra superior con color morado oscuro
        topBar = {
            TopAppBar(
                title = { Text("") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF800080)),
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
            // 🔹 Imagen del logo principal
            Image(
                painter = painterResource(id = R.drawable.logo_playstore),
                contentDescription = "Logo principal",
                modifier = Modifier
                    .width(300.dp)
                    .height(350.dp)
            )

            // 🔹 Texto de bienvenida
            Text(
                text = "Bienvenido",
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif,
                color = Color(0xFF9932CC),  // Morado más claro
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 🔹 Botón para alquilar un vehículo
            Button(
                onClick = { navController.navigate("vehiculos") },
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF800080),  // Morado fuerte
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Alquilar un Vehículo", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))

            // 🔹 Botón para ver vehículos alquilados
            Button(
                onClick = { navController.navigate("vehiculos_rentados") },
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF800080),  // Morado más claro
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Ver mis Vehículos Alquilados", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
