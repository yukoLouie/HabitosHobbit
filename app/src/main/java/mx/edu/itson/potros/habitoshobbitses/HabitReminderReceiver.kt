package mx.edu.itson.potros.habitoshobbitses

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

class HabitReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val habitName = intent.getStringExtra("habit_name") ?: "Hábito"
        Log.d("HabitReminderReceiver", "Notification triggered for: $habitName")

        val notificationManager = context.getSystemService(NotificationManager::class.java)

        val notification = NotificationCompat.Builder(context, "habit_reminder")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("¡Hora de completar tu hábito!")
            .setContentText("Recuerda: $habitName.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(habitName.hashCode(), notification)
    }
}

