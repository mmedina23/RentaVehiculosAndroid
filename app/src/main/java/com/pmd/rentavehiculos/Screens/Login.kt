package com.pmd.rentavehiculos.Screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pmd.rentavehiculos.R

@Composable
fun LoginFuncion(){
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
                        ), // Aquí se definen los colores del degradado
                        start = androidx.compose.ui.geometry.Offset(
                            0f,
                            0f
                        ), // punto de inicio del degradado
                        end = androidx.compose.ui.geometry.Offset(
                            1f,
                            1f
                        ) // punto de finalización del degradado
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

        Button(onClick = { /*TODO*/ }, modifier = Modifier
            .offset(y = 750.dp, x = 145.dp)) {
            Text(text = "Aceptar")
        }
    }
}

@Preview
@Composable
fun previewLogin(){
    LoginFuncion()
}