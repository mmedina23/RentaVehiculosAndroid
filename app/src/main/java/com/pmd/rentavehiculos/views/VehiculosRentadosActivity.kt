package com.pmd.rentavehiculos.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class VehiculosRentadosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token = intent.getStringExtra("TOKEN") ?: ""
        val personaId = intent.getIntExtra("PERSONA_ID", 0) // ✅ Obtiene personaId correctamente

        setContent {
            VehiculosRentadosScreen(token, personaId) // ✅ Se pasa personaId
        }
    }
}
