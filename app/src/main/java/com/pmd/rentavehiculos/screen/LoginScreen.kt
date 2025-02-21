package com.pmd.rentavehiculos.screen

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
import com.pmd.rentavehiculos.vista.VistaLogin

@Composable
fun LoginScreen(navController: NavController, viewModel: VistaLogin = viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0077B7), Color(0xFF003366))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "DriveGo🚗",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(160.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomTextField(
                value = username,
                onValueChange = { username = it },
                label = "Correo electrónico",
                leadingIcon = Icons.Default.Email
            )

            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Contraseña",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (username.isNotEmpty() && password.isNotEmpty()) {
                        viewModel.login(username, password) { success, perfil ->
                            if (success) {
                                navController.navigate(if (perfil == "ADMIN") "menu_admin" else "menu_cliente")
                            } else {
                                errorMessage = "Credenciales incorrectas"
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        errorMessage = "Completa los campos."
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    }
                },
                enabled = username.isNotEmpty() && password.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(50.dp)
                    .shadow(8.dp, shape = RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00A86B),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Iniciar Sesión", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(onClick = { /* Implementar recuperación de contraseña */ }) {
                Text("¿Olvidaste tu contraseña?", color = Color.White, fontSize = 14.sp)
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
        label = { Text(label, color = Color.White) },
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null, tint = Color.White) },
        visualTransformation = visualTransformation,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 6.dp)
            .background(Color.Transparent),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
            cursorColor = Color.White
        )
    )
}
