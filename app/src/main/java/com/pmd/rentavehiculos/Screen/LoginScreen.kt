package com.pmd.rentavehiculos.Screen

import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmd.rentavehiculos.Viewmodel.LoginViewModel

@Composable
fun <NavHostController> LoginScreen(navController: NavHostController) {
    val loginViewModel: LoginViewModel = viewModel()

    val loginState = loginViewModel.loginState.value
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar Sesión", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        BasicTextField(
            value = usuario,
            onValueChange = { usuario = it },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            textStyle = MaterialTheme.typography.bodyLarge
        )
        BasicTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            textStyle = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            loginViewModel.login(usuario, contrasena)
        }) {
            Text("Iniciar sesión")
        }

        // Mostrar mensajes de error si ocurren
        if (loginState.error != null) {
            Text(loginState.error, color = MaterialTheme.colorScheme.error)
        }

        if (loginState.success) {
            // Después de un login exitoso, navegar a MainScreen
            LaunchedEffect(loginState.success) {
                if (loginState.token != null) {
                    navController.navigate(AppScreens.MainScreen.ruta)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}
