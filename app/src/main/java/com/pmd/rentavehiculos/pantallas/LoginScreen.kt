package com.pmd.rentavehiculos.pantallas

import android.graphics.Color.rgb
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /*Image(
                painter = painterResource(id = R.drawable.logo1),
                contentDescription = "Logo de la aplicación",
                modifier = Modifier
                    .size(280.dp)
            )*/

            CustomTextField(
                value = username,
                onValueChange = { username = it },
                label = "Usuario"

            )

            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    if (username.isNotEmpty() && password.isNotEmpty()) {
                        viewModel.login(username, password) { success , perfil ->
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
                    containerColor = Color(rgb(245, 158, 11)),
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
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color(rgb(252, 211, 77))) },
        visualTransformation = visualTransformation,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(rgb(251, 146, 60)),
            unfocusedBorderColor = Color(rgb(251, 146, 60)).copy(alpha = 0.5f),
            cursorColor = Color(rgb(252, 211, 77))
        )
    )
}

/*
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(navController = NavController(LocalContext.current))
}*/

