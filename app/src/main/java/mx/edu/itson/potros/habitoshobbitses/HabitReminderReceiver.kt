package mx.edu.itson.potros.habitoshobbitses

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class HabitReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val habitName = intent.getStringExtra("habit_name") ?: "Hábito"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, "habit_reminder")
            .setSmallIcon(R.drawable.ic_notification) // Reemplaza con tu ícono
            .setContentTitle("¡Hora de completar un hábito!")
            .setContentText("Recuerda: $habitName.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(habitName.hashCode(), notification)
    }
}
