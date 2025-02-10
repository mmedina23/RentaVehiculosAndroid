package com.pmd.rentavehiculos.components

import androidx.compose.runtime.Composable
import com.pmd.rentavehiculos.viewModel.AutenticacionViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.*  // Importante para usar Material 3 components como Text, OutlinedTextField, Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.pmd.rentavehiculos.data.model.LoginState
import com.pmd.rentavehiculos.navigation.appPantallas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: AutenticacionViewModel = viewModel()) {
    val loginState by viewModel.loginState.collectAsState()
    val navController = rememberNavController()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Inicio de Sesión") }) }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario") }
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation()
                )

                Button(
                    onClick = { viewModel.login(username, password);navController.navigate(
                        appPantallas.PersonaScreen.ruta) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar Sesión")
                }

                // Mostrar estado de la autenticación
                when (loginState) {
                    is LoginState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    is LoginState.Success -> Text("¡Bienvenido!", color = Color.Green, modifier = Modifier.align(Alignment.CenterHorizontally))
                    is LoginState.Error -> Text(
                        text = (loginState as LoginState.Error).message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    else -> {}
                }
            }
        }
    }
}
