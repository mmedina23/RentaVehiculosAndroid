package com.pmd.rentavehiculos.views

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi


class VehiculosRentadosActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Obtener el token que se pasa desde el login
        val token = intent.getStringExtra("TOKEN") ?: ""

        setContent {
            VehiculosRentadosScreen(token)
        }
    }
}

