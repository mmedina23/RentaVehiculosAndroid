package com.pmd.rentavehiculos.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pmd.rentavehiculos.network.Renta

@Composable
fun VehiculoRentadoCard(renta: Renta, onLiberar: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Imagen del vehículo rentado
            AsyncImage(
                model = renta.vehiculo.imagen,
                contentDescription = "Imagen del vehículo rentado",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Información del vehículo rentado
            Text(
                text = "${renta.vehiculo.marca} - Rentado por ${renta.persona.nombre}",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Fecha de renta: ${renta.fechaRenta}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Valor total: ${renta.valorTotalRenta}$",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón para liberar el vehículo
            Button(
                onClick = onLiberar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Liberar")
            }
        }
    }
}
