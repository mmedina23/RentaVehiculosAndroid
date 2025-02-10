package com.pmd.rentavehiculos.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class ClienteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClienteScreen()
        }
    }
}

@Composable
fun ClienteScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Bienvenido Cliente")
    }
}

