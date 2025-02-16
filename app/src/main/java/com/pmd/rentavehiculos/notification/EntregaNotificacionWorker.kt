package com.pmd.rentavehiculos.notification



import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pmd.rentavehiculos.R

class EntregaNotificacionWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val vehiculoNombre = inputData.getString("vehiculo_nombre") ?: "Vehículo"
        val fechaEntrega = inputData.getString("fecha_entrega") ?: "Próximamente"

        mostrarNotificacion(vehiculoNombre, fechaEntrega)

        return Result.success()
    }

    private fun mostrarNotificacion(vehiculo: String, fecha: String) {
        val channelId = "ENTREGA_VEHICULO"
        val notificationId = 101

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notificación de Entrega",
                NotificationManager.IMPORTANCE_HIGH
            )

            val manager = applicationContext.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("📅 Próxima Entrega")
            .setContentText("Tu vehículo $vehiculo debe ser entregado el $fecha")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        NotificationManagerCompat.from(applicationContext).notify(notificationId, builder.build())
    }
}
