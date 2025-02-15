package com.pmd.rentavehiculos.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pmd.rentavehiculos.viewmodel.LoginViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF004D80), Color(0xFF00ACC1)) // Azul degradado
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Iniciar Sesión",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            CustomTextField(
                value = username,
                onValueChange = { username = it },
                label = "Usuario",
                leadingIcon = Icons.Default.Email,
                color = Color.White
            )

            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation(),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    if (username.isNotEmpty() && password.isNotEmpty()) {
                        isLoading = true
                        Log.d("LoginScreen", "➡️ Enviando credenciales: Usuario: $username")

                        viewModel.login(username, password) { success, perfil ->
                            isLoading = false
                            Log.d("LoginScreen", "🔄 Respuesta del Login -> success: $success, perfil: $perfil")

                            if (success) {
                                if (perfil == "ADMIN") {
                                    Log.d("LoginScreen", "✅ Redirigiendo a menu_admin")
                                    navController.navigate("menu_admin")
                                } else {
                                    Log.d("LoginScreen", "✅ Redirigiendo a menu_cliente")
                                    navController.navigate("menu_cliente")
                                }
                            } else {
                                errorMessage = "Error de autenticación"
                                Log.e("LoginScreen", "❌ Autenticación fallida")
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        errorMessage = "Por favor, completa todos los campos."
                        Log.e("LoginScreen", "⚠️ Campos vacíos en login")
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    }
                },
                enabled = username.isNotEmpty() && password.isNotEmpty() && !isLoading,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFA000),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(text = "Iniciar Sesión", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
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
    color: Color,
    isError: Boolean = false // Nueva prop para mostrar errores
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label, color = if (isError) Color.Red else Color.White)
        },
        leadingIcon = {
            Icon(imageVector = leadingIcon, contentDescription = null, tint = if (isError) Color.Red else Color.White)
        },
        visualTransformation = visualTransformation,
        singleLine = true,
        isError = isError, // Manejo de errores visuales
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = if (isError) Color.Red else Color(0xFFFFA000), // Amarillo si está bien, rojo si hay error
            unfocusedBorderColor = if (isError) Color.Red.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.7f),
            cursorColor = Color.White,

        )
    )
}


