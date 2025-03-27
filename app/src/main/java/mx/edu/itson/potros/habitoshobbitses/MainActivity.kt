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
import com.google.firebase.auth.FirebaseAuth
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

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        databaseReference.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (habitSnapshot in snapshot.children) {
                        val habit = habitSnapshot.getValue(Habit::class.java)
                        habit?.let {
                            if (it.reminderTime.isNotBlank() && it.days.isNotBlank()) {
                                val reminderTimeParts = it.reminderTime.split(":").mapNotNull { it.toIntOrNull() }
                                if (reminderTimeParts.size == 2) {
                                    val hour = reminderTimeParts[0]
                                    val minute = reminderTimeParts[1]

                                    val selectedDays = it.days.split(",").map { day -> day.trim() }
                                    val dayMap = mapOf(
                                        "Domingo" to Calendar.SUNDAY,
                                        "Lunes" to Calendar.MONDAY,
                                        "Martes" to Calendar.TUESDAY,
                                        "Miércoles" to Calendar.WEDNESDAY,
                                        "Jueves" to Calendar.THURSDAY,
                                        "Viernes" to Calendar.FRIDAY,
                                        "Sábado" to Calendar.SATURDAY
                                    )

                                    for (day in selectedDays) {
                                        val dayOfWeek = dayMap[day]
                                        if (dayOfWeek != null) {
                                            val calendar = Calendar.getInstance().apply {
                                                set(Calendar.DAY_OF_WEEK, dayOfWeek)
                                                set(Calendar.HOUR_OF_DAY, hour)
                                                set(Calendar.MINUTE, minute)
                                                set(Calendar.SECOND, 0)
                                                if (timeInMillis <= System.currentTimeMillis()) {
                                                    add(Calendar.DAY_OF_MONTH, 7)
                                                }
                                            }

                                            val intent = Intent(this@MainActivity, HabitReminderReceiver::class.java).apply {
                                                putExtra("habit_name", it.name)
                                            }

                                            val pendingIntent = PendingIntent.getBroadcast(
                                                this@MainActivity,
                                                (it.id + dayOfWeek.toString()).hashCode(),
                                                intent,
                                                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                                            )

                                            alarmManager.setRepeating(
                                                AlarmManager.RTC_WAKEUP,
                                                calendar.timeInMillis,
                                                AlarmManager.INTERVAL_DAY * 7,
                                                pendingIntent
                                            )
                                        }
                                    }
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
