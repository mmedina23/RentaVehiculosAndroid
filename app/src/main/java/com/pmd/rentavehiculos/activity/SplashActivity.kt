package com.pmd.rentavehiculos.activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pmd.rentavehiculos.R
import com.pmd.rentavehiculos.screens.SplashScreen

class SplashActivity : ComponentActivity() {

    // Duración del splash en milisegundos (por ejemplo, 2000 = 2 segundos)
    private val splashDuration: Long = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen(
                splashDuration = splashDuration,
                onSplashFinished = {
                    // Después del tiempo definido, inicia LoginActivity
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish() // Termina SplashActivity para que el usuario no vuelva a ella al presionar "Atrás"
                }
            )
        }
    }
}