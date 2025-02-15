package com.pmd.rentavehiculos.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pmd.rentavehiculos.R
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.viewModel.AutenticacionViewModel
import com.pmd.rentavehiculos.data.model.LoginState
import com.pmd.rentavehiculos.navigation.appPantallas
import com.pmd.rentavehiculos.ui.theme.Fondo
import com.pmd.rentavehiculos.data.model.Usuario




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavController, viewModel: AutenticacionViewModel = viewModel()) {
    val loginState by viewModel.loginState.collectAsState()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Inicio de Sesión") }) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Fondo con colores suaves
            Fondo()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mustang2),
                    contentDescription = "Logo"
                )

                // Caja de fondo acristalado con menos desenfoque y más opacidad
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(
                            color = Color.White.copy(alpha = 0.5f),  // 🔹 Más opaco para mejorar legibilidad
                            shape = RoundedCornerShape(16.dp)
                        )
                        .blur(0.9.dp)  // 🔹 Reducimos el blur para mayor claridad
                        .padding(16.dp)
                        .border(1.dp, Color.White.copy(alpha = 0.7f), RoundedCornerShape(16.dp)) // 🔹 Agregamos borde blanco semitransparente
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Campo de usuario
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Usuario", color = Color.Black) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.DarkGray,
                                unfocusedIndicatorColor = Color.LightGray,
                                cursorColor = Color.Black,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            )
                        )

                        // Campo de contraseña
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña", color = Color.Black) },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.DarkGray,
                                unfocusedIndicatorColor = Color.LightGray,
                                cursorColor = Color.Black,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            trailingIcon = {
                                val icon = if (passwordVisible) painterResource(id = android.R.drawable.ic_menu_view)
                                else painterResource(id = android.R.drawable.ic_menu_close_clear_cancel)

                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Image(painter = icon, contentDescription = "Toggle password visibility")
                                }
                            }
                        )

                        // Botón de inicio de sesión
                        Button(
                            onClick = { viewModel.login(username, password) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black.copy(alpha = 0.7f))
                        ) {
                            Text("Iniciar Sesión", color = Color.White)
                        }

                        when (loginState) {
                            is LoginState.Loading -> CircularProgressIndicator(color = Color.Black)

                            is LoginState.Success -> {
                                val authData = (loginState as LoginState.Success).authData
                                val perfil = authData.perfil

                                // 🔹 Navegamos según el perfil
                                when (perfil.uppercase()) {
                                    "CLIENTE" -> navController.navigate("Vista_Cliente")
                                    "ADMIN" -> navController.navigate("Vista_Admin")
                                    else -> Toast.makeText(
                                        LocalContext.current,
                                        "Perfil desconocido: $perfil",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            is LoginState.Error -> Text(
                                text = (loginState as LoginState.Error).message,
                                color = Color.Red,
                                fontSize = 16.sp
                            )

                            else -> {}
                        }
                    }
                }
            }
        }
    }
}
