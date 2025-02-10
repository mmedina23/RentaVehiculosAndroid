package com.pmd.rentavehiculos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmd.rentavehiculos.Api.RetrofitInstance
import com.pmd.rentavehiculos.Core.NavigationWrapper
import com.pmd.rentavehiculos.ui.theme.RentaVehiculosTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RentaVehiculosTheme {
                NavigationWrapper()
            }
        }
    }
}