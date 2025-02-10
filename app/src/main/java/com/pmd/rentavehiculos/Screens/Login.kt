package com.pmd.rentavehiculos.Screens

import android.app.AlertDialog
import android.content.Context
import android.graphics.RuntimeShader
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pmd.rentavehiculos.Api.ApiService
import com.pmd.rentavehiculos.Api.RetrofitInstance
import com.pmd.rentavehiculos.Entity.UserRequest
import com.pmd.rentavehiculos.Entity.Usuario
import com.pmd.rentavehiculos.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope


@Composable
fun LoginFuncion(NavigationClient:(String) -> Unit, NavigationAdmind:(String) -> Unit){
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.DarkGray,
                            Color.Black
                        ),
                        start = androidx.compose.ui.geometry.Offset(
                            0f,
                            0f
                        ),
                        end = androidx.compose.ui.geometry.Offset(
                            1f,
                            1f
                        )
                    )
                )
                .fillMaxWidth()
                .fillMaxHeight()
        )

        Text(
            text = "Renta Vehiculos",
            color = Color.White,
            fontSize = 30.sp,
            fontFamily = SanFrancisco,
            modifier = Modifier
            .offset(y = 70.dp, x = 75.dp))

        Image(
            painter = painterResource(id = R.drawable.coche_amarillo),
            contentDescription = "coche amarillo",
            modifier = Modifier
                .offset(y = 90.dp, x = 15.dp)
                .size(360.dp)
        )
        BasicTextField(
            value = userName,
            onValueChange = { userName = it },
            textStyle = TextStyle(
                color = Color.White,
                fontFamily = SanFrancisco,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .offset(y = 550.dp, x = 32.dp)
                .background(
                    Color.White.copy(alpha = 0.28f),
                    shape = RoundedCornerShape(25.dp)
                )
                .padding(16.dp)
                .width(300.dp),
            cursorBrush = SolidColor(Color(0xFF1B5E20)),
            decorationBox = { innerTextField ->
                if (userName.isEmpty()) {
                    Text(
                        text = "nombre de usuario",
                        color = Color.Gray,
                        fontSize = 18.sp
                    )
                }
                innerTextField()
            }
        )

        BasicTextField(value = password,
            onValueChange = {password = it},
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = SanFrancisco
            ),
            modifier = Modifier
                .offset(y = 630.dp, x = 32.dp)
                .background(
                    Color.White.copy(alpha = 0.28f),
                    shape = RoundedCornerShape(25.dp)
                )
                .padding(16.dp)
                .width(300.dp),
            cursorBrush = SolidColor(Color(0xFF1B5E20)),
            decorationBox = { innerTextField ->
                if (userName.isEmpty()){
                    Text(
                        text = "contraseña",
                        color = Color.Gray,
                        fontSize = 18.sp
                    )
                }
                innerTextField()
            }
        )

        val context = LocalContext.current

        Button(onClick = { enviarUsuario(userName, password, context,NavigationClient, NavigationAdmind) }, modifier = Modifier
            .offset(y = 750.dp, x = 145.dp)) {
            Text(text = "Aceptar")
        }
    }
}

fun enviarUsuario(
    userName: String,
    password: String,
    context: Context,
    NavigationClient: (String) -> Unit,
    NavigationAdmind: (String) -> Unit
) {
    val userRequest = UserRequest(userName, password)

    GlobalScope.launch(Dispatchers.IO) {
        try {
            val service = RetrofitInstance.makeRetrofitService()
            val response = service.login(userRequest)

            if (response.isSuccessful) {
                val usuario = response.body()
                val newLlave = usuario?.llave
                val perfil = usuario?.perfil
                withContext(Dispatchers.Main) {
                    if (newLlave != null) {
                        mostrarAlerta(context, "Llave del usuario", "La llave es: $newLlave")
                        if(perfil == "ADMIN"){
                            println("este es el perfil $perfil")
                            NavigationAdmind(newLlave)
                        }else if(perfil == "CLIENTE"){
                            NavigationClient(newLlave)
                        }
                    } else {
                        mostrarAlerta(context, "Error", "No se pudo obtener la llave.")
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    mostrarAlerta(context, "Error", "Código de error: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                mostrarAlerta(context, "Error", "Excepción: ${e.message}")
            }
        }
    }
}



fun mostrarAlerta(context: Context, title: String, message: String) {
    // Crear un AlertDialog
    val alertDialog = AlertDialog.Builder(context)
        .setTitle(title)  // Título del alerta
        .setMessage(message)  // Mensaje del alerta
        .setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()  // Cierra el alerta al presionar "Aceptar"
        }
        .create()

    // Mostrar el AlertDialog
    alertDialog.show()
}



/*
    LaunchedEffect(Unit) {
        try {
            val usuarioRequest = UserRequest(userName, password)
            val service = RetrofitInstance.makeRetrofitService()
            val response = service.login(usuarioRequest)
            val usuario = response.body()
            val token = usuario?.llave
        }catch (e : Exception){
            println(e)
        }
    }*/

@Preview
@Composable
fun previewLogin(){
    LoginFuncion(NavigationClient = {}, NavigationAdmind = {})
}