package com.pmd.rentavehiculos

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pmd.rentavehiculos.views.SplashActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Redirige automáticamente a SplashActivity al iniciar la app
        startActivity(Intent(this, SplashActivity::class.java))
        finish() // Finaliza MainActivity para que no quede en el historial
    }
}
