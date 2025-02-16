package com.pmd.rentavehiculos.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
import com.pmd.rentavehiculos.viewmodel.LoginViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo1),
                contentDescription = "Logo de la aplicación",
                modifier = Modifier
                    .size(280.dp)
            )

            CustomTextField(
                value = username,
                onValueChange = { username = it },
                label = "Usuario",
                leadingIcon = Icons.Default.Email
            )

            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {

                    if (username.isNotEmpty() && password.isNotEmpty()) {
                        viewModel.login(username, password) { success, perfil ->

                            if (success) {
                                if (perfil == "ADMIN") {
                                    navController.navigate("menu_admin")
                                } else {
                                    navController.navigate("menu_cliente")
                                }
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

                enabled = username.isNotEmpty() && password.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(42.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0077B7),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Iniciar Sesión", fontWeight = FontWeight.Bold, fontSize = 14.sp)
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
        label = { Text(label, color = Color(0xFF0077B7)) },
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null, tint = Color(0xFF0D47A1)) },
        visualTransformation = visualTransformation,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF005F99),
            unfocusedBorderColor = Color(0xFF0D47A1).copy(alpha = 0.5f),
            cursorColor = Color(0xFF005F99)
        )
    )
}

