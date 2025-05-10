package mx.edu.itson.potros.habitoshobbitses

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class HabitNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val titulo = intent.getStringExtra("titulo") ?: "Hábito"
        val mensaje = intent.getStringExtra("mensaje") ?: "Hora del hábito"
        val notificationId = intent.getIntExtra("notificationId", (System.currentTimeMillis() % 10000).toInt())

        android.util.Log.d("HabitNotificationReceiver", "Notification received: id=$notificationId, title=$titulo, message=$mensaje")

        NotificationHelper.showNotification(
            context,
            notificationId,
            titulo,
            mensaje
        )
    }
}

