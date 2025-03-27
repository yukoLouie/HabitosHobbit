package mx.edu.itson.potros.habitoshobbitses

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        crearCanalDeNotificaciones()

        val btnIngresar = findViewById<Button>(R.id.btn_ingresar)
        val btnRegistrar = findViewById<Button>(R.id.btn_registrar)

        btnIngresar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        btnRegistrar.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        programarNotificaciones()
    }

    private fun programarNotificaciones() {
        databaseReference = FirebaseDatabase.getInstance().getReference("habits")
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (habitSnapshot in snapshot.children) {
                    val habit = habitSnapshot.getValue(Habit::class.java)
                    habit?.let {
                        if (it.reminderTime.isNotBlank()) { // Ensure reminderTime is not empty
                            val reminderTimeParts = it.reminderTime.split(":").mapNotNull { timePart -> timePart.toIntOrNull() }
                            if (reminderTimeParts.size == 2) {
                                val hour = reminderTimeParts[0]
                                val minute = reminderTimeParts[1]

                                val calendar = Calendar.getInstance().apply {
                                    set(Calendar.HOUR_OF_DAY, hour)
                                    set(Calendar.MINUTE, minute)
                                    set(Calendar.SECOND, 0)
                                    if (timeInMillis <= System.currentTimeMillis()) {
                                        add(Calendar.DAY_OF_MONTH, 1) // Schedule for the next day if the time has passed
                                    }
                                }

                                val intent = Intent(this@MainActivity, HabitReminderReceiver::class.java).apply {
                                    putExtra("habit_name", it.name)
                                }

                                val pendingIntent = PendingIntent.getBroadcast(
                                    this@MainActivity,
                                    it.id.hashCode(),
                                    intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                                )

                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error al programar notificaciones", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun crearCanalDeNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "habit_reminder",
                "Recordatorio de hábitos",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }
}
