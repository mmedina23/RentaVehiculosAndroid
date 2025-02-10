package com.pmd.rentavehiculos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.pmd.rentavehiculos.nav.Navigation
import com.pmd.rentavehiculos.viewmodels.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val nav = rememberNavController()
            val loginVM: LoginViewModel by viewModels()

            Navigation(nav, loginVM)
        }
    }
}