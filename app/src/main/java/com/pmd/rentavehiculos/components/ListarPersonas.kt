package com.pmd.rentavehiculos.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pmd.rentavehiculos.data.model.Persona
import com.pmd.rentavehiculos.viewModel.PersonaViewModel






@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PersonaScreen(viewModel: PersonaViewModel = viewModel()) {
    val personas = viewModel.personas.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Personas") })
        }
    ) {
        if (personas.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(personas) { persona ->
                    PersonaItem(persona)
                }
            }
        }
    }
}

@Composable
fun PersonaItem(persona: Persona) {
    Card(
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = " Id : ${persona.id} ", style = MaterialTheme.typography.headlineSmall )
            Text(text = "Nombre: ${persona.nombre}")
            Text(text = "Apellidos: ${persona.apellidos}")
            Text(text = "direccion : ${persona.direccion}")
            Text(text = "telefono: ${persona.telefono}")
            Text(text = " tipoIdentificacion: ${persona.tipoIdentificacion} " )
            Text(text = " identificacion: ${persona.identificacion} " )
        }
    }
}