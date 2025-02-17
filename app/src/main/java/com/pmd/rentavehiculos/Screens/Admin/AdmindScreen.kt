package com.pmd.rentavehiculos.Screens.Admin

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.pmd.rentavehiculos.Entity.Usuario

@Composable
fun AdmindScreen(token: String) {
    Text(text = "hola usuario admind con el toke numero $token")
}