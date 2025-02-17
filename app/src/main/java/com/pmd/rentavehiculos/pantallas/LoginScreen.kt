package com.pmd.rentavehiculos.pantallas

/*
Jetpack Compose es un framework moderno de UI para Android desarrollado por Google.
Permite construir interfaces de usuario de forma declarativa, simplificando la creación y
gestión de componentes visuales.
*/

// Importaciones necesarias para la interfaz de usuario con Jetpack Compose
import android.graphics.Color.rgb
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.viewmodel.LoginViewModel

// Composable principal para la pantalla de inicio de sesión
@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    // Variables de estado para el usuario, contraseña y mensaje de error
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current // Obtener el contexto para mostrar Toasts

    // Contenedor principal que ocupa toda la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.logo4), // Se debe asegurar que la imagen esté en res/drawable
            contentDescription = null, // Se deja null porque es decorativa
            contentScale = ContentScale.Crop, // Ajusta la imagen para cubrir toda la pantalla
            modifier = Modifier.matchParentSize() // La imagen ocupa toda la pantalla
        )

        // Contenedor de los elementos del formulario de inicio de sesión
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp), // Margen horizontal
            verticalArrangement = Arrangement.Center, // Centra verticalmente el contenido
            horizontalAlignment = Alignment.CenterHorizontally // Centra horizontalmente el contenido
        ) {
            // Campo de texto para ingresar el nombre de usuario
            CustomTextField(
                value = username,
                onValueChange = { username = it }, // Actualiza el estado al escribir
                label = "Usuario"
            )

            // Campo de texto para ingresar la contraseña
            CustomTextField(
                value = password,
                onValueChange = { password = it }, // Actualiza el estado al escribir
                label = "Contraseña",
                visualTransformation = PasswordVisualTransformation() // Oculta la contraseña
            )

            Spacer(modifier = Modifier.height(18.dp)) // Espaciado entre campos y botón

            // Botón de inicio de sesión
            Button(
                onClick = {
                    // Verifica que los campos no estén vacíos
                    if (username.isNotEmpty() && password.isNotEmpty()) {
                        // Llamar al ViewModel para procesar el inicio de sesión
                        viewModel.login(username, password) { success, perfil ->
                            if (success) {
                                // Redirigir según el tipo de perfil
                                if (perfil == "ADMIN") {
                                    navController.navigate("menu_admin") // Navega a la pantalla de administrador
                                } else {
                                    navController.navigate("menu_cliente") // Navega a la pantalla de cliente
                                }
                            } else {
                                // Mostrar mensaje de error si las credenciales son incorrectas
                                errorMessage = "Error de autenticación"
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        // Mensaje de error si los campos están vacíos
                        errorMessage = "Por favor, completa todos los campos."
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    }
                },
                enabled = username.isNotEmpty() && password.isNotEmpty(), // Deshabilita el botón si los campos están vacíos
                modifier = Modifier
                    .fillMaxWidth(0.6f) // Ancho del botón al 60% del contenedor
                    .height(42.dp), // Altura del botón
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(rgb(245, 158, 11)), // Color de fondo del botón
                    contentColor = Color.White // Color del texto del botón
                ),
                shape = RoundedCornerShape(8.dp) // Esquinas redondeadas del botón
            ) {
                Text(text = "Iniciar Sesión", fontWeight = FontWeight.Bold, fontSize = 14.sp) // Texto del botón
            }

            // Muestra un mensaje de error si existe
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red, // Texto en color rojo para indicar error
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 6.dp) // Espaciado superior
                )
            }
        }
    }
}

// Componente reutilizable para los campos de texto en el formulario de inicio de sesión
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String, // Valor del campo de texto
    onValueChange: (String) -> Unit, // Callback para actualizar el estado del campo
    label: String, // Etiqueta del campo
    visualTransformation: VisualTransformation = VisualTransformation.None // Transforma el texto si es necesario (por ejemplo, para contraseñas)
) {
    OutlinedTextField(
        value = value, // Asigna el valor del estado
        onValueChange = onValueChange, // Actualiza el estado al escribir
        label = { Text(label, color = Color.White) }, // Muestra la etiqueta con color blanco
        visualTransformation = visualTransformation, // Permite ocultar el texto si es contraseña
        singleLine = true, // Restringe a una sola línea
        modifier = Modifier
            .fillMaxWidth() // Ocupar todo el ancho disponible
            .padding(vertical = 4.dp), // Espaciado entre campos
        shape = RoundedCornerShape(8.dp), // Bordes redondeados del campo de texto
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(rgb(251, 146, 60)), // Color del borde cuando está enfocado
            unfocusedBorderColor = Color(rgb(251, 146, 60)).copy(alpha = 0.5f), // Color del borde cuando no está enfocado
            cursorColor = Color(rgb(252, 211, 77)) // Color del cursor
        )
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(navController = NavController(LocalContext.current)) // Vista previa de la pantalla de inicio de sesión
}

