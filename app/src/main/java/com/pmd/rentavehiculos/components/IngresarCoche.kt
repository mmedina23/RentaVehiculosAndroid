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
fun IngresarCoche() {
    // Variables de estado para usuario y contraseña
    var id by remember { mutableStateOf("") }
    var marca by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var carroceria by remember { mutableStateOf("") }
    var plazas by remember { mutableStateOf("") }
    var cambios by remember { mutableStateOf("") }
    var tipoCombustible by remember { mutableStateOf("") }
    var valorDia by remember { mutableStateOf("") }
    var disponible by remember { mutableStateOf("") }

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
                value = marca,
                onValueChange = { marca = it },
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
                value = color,
                onValueChange = {color = it },
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
                value = carroceria,
                onValueChange = { carroceria = it },
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
                value = plazas ,
                onValueChange = { plazas  = it },
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
                value = cambios,
                onValueChange = { cambios = it },
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
                value = tipoCombustible,
                onValueChange = { tipoCombustible = it },
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
                value = valorDia,
                onValueChange = { valorDia = it },
                label = { Text("Valor por dia")  },
                visualTransformation = PasswordVisualTransformation(), // Oculta el texto de la contraseña
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
        }
        item {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                value = disponible,
                onValueChange = { disponible = it },
                label = { Text("Disponible")  },
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
                Text("Ingresar")
            }
        }
    }
}