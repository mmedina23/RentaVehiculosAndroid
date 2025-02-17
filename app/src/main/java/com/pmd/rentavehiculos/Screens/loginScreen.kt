package com.pmd.rentavehiculos.Screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.modelos.LoginRequest
import com.pmd.rentavehiculos.retrofit.RetrofitClient
import com.pmd.rentavehiculos.viewmodels.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(
    navController: NavController,
    // Recibimos el LoginViewModel
    loginViewModel: LoginViewModel
) {
    val context = LocalContext.current

    // Estados locales para username, password y error
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.vehiculo),
                contentDescription = "App Logo",
                modifier = Modifier.size(250.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            SimpleCustomTextField(
                value = username,
                onValueChange = { username = it },
                label = "Nombre de usuario",
                leadingIcon = Icons.Default.AccountCircle,
                keyboardType = KeyboardType.Text
            )

            SimpleCustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation(),
                keyboardType = KeyboardType.Password
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (username.isEmpty() || password.isEmpty()) {
                        errorMessage = "Por favor, completa todos los campos."
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    } else {
                        // Llamamos a la función loginUser pasando el ViewModel
                        loginUser(
                            username,
                            password,
                            navController,
                            context,
                            loginViewModel
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Iniciar Sesión",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}

fun loginUser(
    username: String,
    password: String,
    navController: NavController,
    context: Context,
    loginViewModel: LoginViewModel
) {
    val retrofitService = RetrofitClient.apiService
    val loginRequest = LoginRequest(nombre_usuario = username, contrasena = password)

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = retrofitService.loginUser(loginRequest)
            withContext(Dispatchers.Main) {
               //guardo apiKey EN EL VIEWMODEL
                loginViewModel.apiKey.value = response.llave
                loginViewModel.usuario.value = response.persona
                loginViewModel.perfil.value = response.perfil

                Log.d("API_DEBUG", "API Key recibida: ${response.llave}")

                // segun perfil ir a blabla o blabla
                when (response.perfil.uppercase()) {
                    "ADMIN" -> navController.navigate("adminOpciones")
                    "CLIENTE" -> navController.navigate("ListaVehiculos")
                    else -> Toast.makeText(context, "Rol desconocido", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error en el login: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleCustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(label, color = Color.Black) },
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null, tint = Color.Black) },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Gray,
            unfocusedTextColor = Color.Black
        )
    )
}
