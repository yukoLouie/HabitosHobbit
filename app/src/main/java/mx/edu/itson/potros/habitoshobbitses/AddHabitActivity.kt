package mx.edu.itson.potros.habitoshobbitses

import android.app.AlertDialog
import android.app.TimePickerDialog
import mx.edu.itson.potros.habitoshobbitses.R
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar


class AddHabitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_habit)

        val btnGuardar = findViewById<Button>(R.id.btn_guardar)
        val btnCancelar = findViewById<Button>(R.id.btn_cancelar)

        // Código avanzado comentado
        /*
        val btnGuardar = findViewById<Button>(R.id.btn_guardar)
        btnGuardar.setOnClickListener {
            // Lógica para guardar el hábito
        }
        */}

    private fun mostrarTimePicker() {
        val calendar = Calendar.getInstance()
        val horaActual = calendar.get(Calendar.HOUR_OF_DAY)
        val minutoActual = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            this@AddHabitActivity, // Aquí especificamos claramente el contexto
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
        val numberPicker = NumberPicker(this@AddHabitActivity).apply {
            minValue = 1
            maxValue = 120
            value = 15
        }

        AlertDialog.Builder(this@AddHabitActivity)
            .setTitle("Duración del hábito (minutos)")
            .setView(numberPicker)
            .setPositiveButton("Aceptar") { _, _ ->
                val duracion = numberPicker.value

                NotificationScheduler.programarInicioYFinDeHábito(
                    context = this@AddHabitActivity,
                    inicioEnMilis = inicioEnMillis,
                    duracionEnMinutos = duracion,
                    tituloInicio = "¡Hora del hábito!",
                    mensajeInicio = "Es hora de iniciar tu hábito 🕒",
                    tituloFin = "¡Bien hecho!",
                    mensajeFin = "Completaste tu hábito ✅"
                )

                Toast.makeText(this@AddHabitActivity, "Hábito programado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

}

