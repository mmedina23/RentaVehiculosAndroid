package com.pmd.rentavehiculos.ui.theme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pmd.rentavehiculos.data.network.RentaService
import com.pmd.rentavehiculos.data.repository.SessionManager
import com.pmd.rentavehiculos.data.network.VehiculoService

class ClienteViewModelFactory(
    private val rentaService: RentaService,
    private val vehiculoService: VehiculoService,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClienteViewModel::class.java)) {
            return ClienteViewModel(rentaService, vehiculoService, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
