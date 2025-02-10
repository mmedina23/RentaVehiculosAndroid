package com.pmd.rentavehiculos.Screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AdmindScreen(token: String) {
    Text(text = "hola usuario admind con el toke numero $token")
}

@Preview
@Composable
fun previewAdmind(){
    AdmindScreen(token = "")
}