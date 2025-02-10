package com.pmd.rentavehiculos.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.pmd.rentavehiculos.R

class SplashActivity : AppCompatActivity() {

    // Duración del splash en milisegundos (por ejemplo, 2000 = 2 segundos)
    private val splashDuration: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Después del tiempo definido, inicia LoginActivity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Termina SplashActivity para que el usuario no vuelva a ella al presionar "Atrás"
        }, splashDuration)
    }
}