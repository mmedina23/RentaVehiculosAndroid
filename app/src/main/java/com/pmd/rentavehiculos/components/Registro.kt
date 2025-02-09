package com.pmd.rentavehiculos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.*  // Importante para usar Material 3 components como Text, OutlinedTextField, Button
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun Registro() {
    // Variables de estado para usuario y contraseña
    var id by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var tipoIdentificacion by remember { mutableStateOf("") }
    var identificacion by remember { mutableStateOf("") }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp) // Espaciado entre los campos
    ) {

        item {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                value = id,
                onValueChange = { id = it },
                label = { Text("Id") }
            )
        }

        item {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                value = id,
                onValueChange = { id = it },
                label = { Text("Marca")  },
                visualTransformation = PasswordVisualTransformation(), // Oculta el texto de la contraseña
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }
        item {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                value = nombre,
                onValueChange = {nombre = it },
                label = { Text("Color")  },
                visualTransformation = PasswordVisualTransformation(), // Oculta el texto de la contraseña
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }
        item {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text("Carroceria")  },
                visualTransformation = PasswordVisualTransformation(), // Oculta el texto de la contraseña
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }
        item {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                value = direccion ,
                onValueChange = { direccion  = it },
                label = { Text("plazas")  },
                visualTransformation = PasswordVisualTransformation(), // Oculta el texto de la contraseña
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }
        item {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Cambios")  },
                visualTransformation = PasswordVisualTransformation(), // Oculta el texto de la contraseña
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }
        item {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                value = tipoIdentificacion,
                onValueChange = { tipoIdentificacion = it },
                label = { Text("tipo de combustible")  },
                visualTransformation = PasswordVisualTransformation(), // Oculta el texto de la contraseña
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }
        item {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                value = identificacion,
                onValueChange = { identificacion = it },
                label = { Text("Valor por dia")  },
                visualTransformation = PasswordVisualTransformation(), // Oculta el texto de la contraseña
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }


        item {
            Button(
                onClick = { /* Acción de login aquí */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Registrar")
            }
        }
    }
}