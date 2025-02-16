package com.pmd.rentavehiculos.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.pmd.rentavehiculos.model.Vehiculo
import com.pmd.rentavehiculos.network.Renta

@Composable
fun VehiculoItem(vehiculo: Vehiculo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(vehiculo.imagen),
                contentDescription = "Imagen del vehículo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${vehiculo.marca} - ${vehiculo.carroceria}")
        }
    }
}
//para rentar
@Composable
fun VehiculoItemConBoton(vehiculo: Vehiculo, textoBoton: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(vehiculo.imagen),
                contentDescription = "Imagen del vehículo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${vehiculo.marca} - ${vehiculo.carroceria}")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
                Text(textoBoton)
            }
        }
    }
}

@Composable
fun VehiculoRentadoItem(renta: Renta) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(renta.vehiculo.imagen),
                contentDescription = "Imagen del vehículo rentado",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${renta.vehiculo.marca} - Rentado por: ${renta.persona.nombre}")
        }
    }
}
