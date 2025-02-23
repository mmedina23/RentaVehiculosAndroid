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
    loginViewModel: LoginViewModel
) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.vehiculo),
                contentDescription = "App Logo",
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            CustomTextField(
                value = username,
                onValueChange = { username = it },
                label = "Nombre de usuario",
                leadingIcon = Icons.Default.AccountCircle,
                keyboardType = KeyboardType.Text
            )

            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation(),
                keyboardType = KeyboardType.Password
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (username.isEmpty() || password.isEmpty()) {
                        errorMessage = "Por favor, completa todos los campos."
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    } else {
                        loginUser(username, password, navController, context, loginViewModel)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Iniciar Sesión", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

fun loginUser(
    username: String,
    password: String,
    navController: NavController,
    context: Context,      // Contexto de la app para mostrar mensajes en pantalla
    loginViewModel: LoginViewModel      // ViewModel donde guardo el usuario
) {

    // Conecto con la api
    val retrofitService = RetrofitClient.apiService
    //creo la solicitud de login
    val loginRequest = LoginRequest(nombre_usuario = username, contrasena = password)


    // ejecuto en un hilo aparte para no bloquear el hilo principal(la propia app)
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = retrofitService.loginUser(loginRequest) // Llamo a la api y espera la respuesta
            withContext(Dispatchers.Main) { //aqui cambio al hilo principal para actualizar la interfaz de usuario

                //guardo la apikey, el usuario y el tipo de perfil en el viewModel
                loginViewModel.apiKey.value = response.llave
                loginViewModel.usuario.value = response.persona
                loginViewModel.perfil.value = response.perfil

                //depuro mostrando en el logCat la api
                Log.d("API_DEBUG", "API Key recibida: ${response.llave}")

                when (response.perfil.uppercase()) { //compruebo el perfil del usuario y lo llevo a donde admin o cliente
                    "ADMIN" -> navController.navigate("adminOpciones")
                    "CLIENTE" -> navController.navigate("ListaVehiculos")
                    else -> Toast.makeText(context, "Rol desconocido", Toast.LENGTH_SHORT).show() //mensaje de error
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
fun CustomTextField(
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
        label = { Text(label, color = Color.Gray) },
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null, tint = Color.Gray) },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.LightGray,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
        ),
        shape = RoundedCornerShape(12.dp)
    )
}