package com.pmd.rentavehiculos.pantallas

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.viewModels.LoginViewModel

/**
 * Pantalla de inicio de sesión para los usuarios.
 * Permite autenticarse con un nombre de usuario y contraseña.
 */
@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    // Estados para almacenar el nombre de usuario, la contraseña y los mensajes de error
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current // Obtiene el contexto de la aplicación

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // Fondo blanco de la pantalla
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo de la aplicación
            Image(
                painter = painterResource(id = R.drawable.logo_playstore),
                contentDescription = "Logo de la aplicación",
                modifier = Modifier.size(280.dp)
            )

            // Campo de texto para ingresar el usuario
            CustomTextField(
                value = username,
                onValueChange = { username = it },
                label = "Usuario",
                leadingIcon = Icons.Default.Email
            )

            // Campo de texto para ingresar la contraseña
            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation() // Oculta la contraseña
            )

            Spacer(modifier = Modifier.height(28.dp)) // Espaciado entre los elementos

            // Botón de inicio de sesión
            Button(
                onClick = {
                    if (username.isNotEmpty() && password.isNotEmpty()) {
                        viewModel.login(username, password) { success, perfil ->
                            if (success) {
                                // Redirigir al menú correspondiente según el perfil del usuario
                                val destino =
                                    if (perfil == "ADMIN") "menu_admin" else "menu_cliente"
                                navController.navigate(destino)
                            } else {
                                errorMessage = "Error de autenticación"
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        errorMessage = "Por favor, completa todos los campos."
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f) // Tamaño reducido del botón
                    .height(42.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF800080), // Color morado fijo
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp) // Bordes redondeados
            ) {
                Text(text = "Iniciar Sesión", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            // Mensaje de error en caso de fallo en la autenticación
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

/**
 * Campo de texto reutilizable con icono y formato personalizado.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color(0xFF800080)) }, // Morado fuerte para el texto del label
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = Color(0xFF800080)
            )
        },
        visualTransformation = visualTransformation,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp), // Espaciado entre los campos de texto
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF9932CC), // Morado claro cuando está enfocado
            unfocusedBorderColor = Color(0xFF8A2BE2).copy(alpha = 0.5f), // Morado oscuro con transparencia
            cursorColor = Color(0xFF800080) // Color del cursor en morado fuerte
        )
    )
}
