package mx.edu.itson.potros.habitoshobbitses

import android.app.*
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class AddHabitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_habit)

        pedirPermisosNotificaciones()

        val btnProgramar = findViewById<Button>(R.id.btn_programar_habito)
        btnProgramar.setOnClickListener {
            mostrarTimePicker()
        }
    }

    private fun mostrarTimePicker() {
        val calendar = Calendar.getInstance()
        val horaActual = calendar.get(Calendar.HOUR_OF_DAY)
        val minutoActual = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            this@AddHabitActivity,
            { _, hourOfDay, minute ->
                val inicioCalendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                }

                mostrarDuracionPicker(inicioCalendar.timeInMillis)
            },
            horaActual,
            minutoActual,
            true
        ).show()
    }

    private fun mostrarDuracionPicker(inicioEnMillis: Long) {
        val picker = NumberPicker(this@AddHabitActivity).apply {
            minValue = 1
            maxValue = 120
            value = 15
        }

        AlertDialog.Builder(this@AddHabitActivity)
            .setTitle("Duración del hábito (minutos)")
            .setView(picker)
            .setPositiveButton("Aceptar") { _, _ ->
                val duracion = picker.value
                NotificationScheduler.programarInicioYFinDeHábito(
                    context = this@AddHabitActivity,
                    inicioEnMilis = inicioEnMillis,
                    duracionEnMinutos = duracion,
                    tituloInicio = "¡Hora del hábito!",
                    mensajeInicio = "Es hora de comenzar tu hábito 💪",
                    tituloFin = "¡Buen trabajo!",
                    mensajeFin = "Terminaste tu hábito 🏁"
                )
                Toast.makeText(this, "Notificación programada", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun pedirPermisosNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
    }
}
