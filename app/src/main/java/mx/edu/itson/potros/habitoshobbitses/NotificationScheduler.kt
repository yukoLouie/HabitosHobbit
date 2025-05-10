package mx.edu.itson.potros.habitoshobbitses

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

object NotificationScheduler {
    fun programarNotificacion(context: Context, tiempoEnMilis: Long, titulo: String, mensaje: String) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("titulo", titulo)
            putExtra("mensaje", mensaje)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            (tiempoEnMilis % 10000).toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, tiempoEnMilis, pendingIntent)
    }

    fun programarInicioYFinDeHábito(context: Context, inicioEnMilis: Long, duracionEnMinutos: Int, tituloInicio: String, mensajeInicio: String, tituloFin: String, mensajeFin: String) {
        programarNotificacion(context, inicioEnMilis, tituloInicio, mensajeInicio)
        programarNotificacion(context, inicioEnMilis + duracionEnMinutos * 60 * 1000, tituloFin, mensajeFin)
    }
}

