package mx.edu.itson.potros.habitoshobbitses

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object NotificationScheduler {

    fun programarNotificacion(
        context: Context,
        tiempoEnMilis: Long,
        titulo: String,
        mensaje: String
    ) {
        val intent = Intent(context, HabitNotificationReceiver::class.java).apply {
            putExtra("titulo", titulo)
            putExtra("mensaje", mensaje)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        alarmManager?.setExact(AlarmManager.RTC_WAKEUP, tiempoEnMilis, pendingIntent)
    }

    fun programarInicioYFinDeHábito(
        context: Any,
        inicioEnMilis: Long,
        duracionEnMinutos: Int,
        tituloInicio: String,
        mensajeInicio: String,
        tituloFin: String,
        mensajeFin: String
    ) {

    }
}
