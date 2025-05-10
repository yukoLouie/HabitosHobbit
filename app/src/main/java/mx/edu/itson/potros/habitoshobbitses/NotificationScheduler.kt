package mx.edu.itson.potros.habitoshobbitses

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent


object NotificationScheduler {
    fun programarNotificacion(context: Context, tiempoEnMilis: Long, titulo: String, mensaje: String, notificationId: Int) {
        NotificationHelper.createNotificationChannel(context)

        android.util.Log.d("NotificationScheduler", "Scheduling notification id=$notificationId at time=$tiempoEnMilis")

        val intent = Intent(context, HabitNotificationReceiver::class.java).apply {
            putExtra("titulo", titulo)
            putExtra("mensaje", mensaje)
            putExtra("notificationId", notificationId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        try {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, tiempoEnMilis, pendingIntent)
        } catch (e: Exception) {
            android.util.Log.e("NotificationScheduler", "setExactAndAllowWhileIdle failed, falling back to set()", e)
            alarmManager.set(AlarmManager.RTC_WAKEUP, tiempoEnMilis, pendingIntent)
        }
    }

    fun programarInicioYFinDeHábito(context: Context, inicioEnMilis: Long, duracionEnMinutos: Int, tituloInicio: String, mensajeInicio: String, tituloFin: String, mensajeFin: String, notificationIdStart: Int, notificationIdEnd: Int) {
        programarNotificacion(context, inicioEnMilis, tituloInicio, mensajeInicio, notificationIdStart)
        programarNotificacion(context, inicioEnMilis + duracionEnMinutos * 60 * 1000, tituloFin, mensajeFin, notificationIdEnd)
    }
}

