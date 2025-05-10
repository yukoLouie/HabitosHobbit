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
        NotificationHelper.showNotification(
            context,
            habitName.hashCode(),
            "¡Hora de completar tu hábito!",
            "Recuerda: $habitName."
        )
    }
}

