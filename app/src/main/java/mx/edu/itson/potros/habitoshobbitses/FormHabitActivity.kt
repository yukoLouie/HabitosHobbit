package mx.edu.itson.potros.habitoshobbitses
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class FormHabitActivity : AppCompatActivity() {

    private var habitId: String? = null // ID del hábito para edición (si es necesario)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_habit)

        // Referencias a los elementos del layout
        val nameInput = findViewById<EditText>(R.id.et_habit_name)
        val descriptionInput = findViewById<EditText>(R.id.et_habit_description)
        val frequencyInput = findViewById<EditText>(R.id.et_habit_frequency)
        val categorySpinner = findViewById<Spinner>(R.id.spinner_category)
        val iconoColorTextView = findViewById<TextView>(R.id.tv_icono_color)
        val timePicker = findViewById<EditText>(R.id.et_hora_recordatorio)
        val saveButton = findViewById<Button>(R.id.btn_save_habit)
        val cancelButton = findViewById<Button>(R.id.btn_cancel_habit)

        // Configurar Spinner de categorías
        val categories = mutableListOf("Personal", "Salud", "Productividad")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        // Verifica si se está editando un hábito (habitId pasado en Intent)
        habitId = intent.getStringExtra("habit_id")
        if (habitId != null) {
            // Si hay un habitId, carga el hábito para editarlo
            cargarDatosDelHabito(habitId!!, nameInput, descriptionInput, frequencyInput, categorySpinner)
        }

        // Selector de ícono/color
        iconoColorTextView.setOnClickListener {
            val options = arrayOf("Rojo", "Azul", "Verde")
            AlertDialog.Builder(this)
                .setTitle("Selecciona un ícono o color")
                .setItems(options) { _, which ->
                    iconoColorTextView.text = "Ícono/Color: ${options[which]}"
                }
                .show()
        }

        // Selector de hora de recordatorio
        timePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    timePicker.setText(String.format("%02d:%02d", hourOfDay, minute))
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePickerDialog.show()
        }

        // Guardar hábito
        saveButton.setOnClickListener {
            val name = nameInput.text.toString()
            val description = descriptionInput.text.toString()
            val frequency = frequencyInput.text.toString().toIntOrNull()
            val category = categorySpinner.selectedItem.toString()
            val icon = "default" // Cambia según tu lógica

            if (!name.isNullOrEmpty() && frequency != null) {
                val habit = Habit(
                    name = name,
                    description = description,
                    frequency = frequency,
                    category = category,
                    icon = icon,
                    completedToday = false,
                    createdAt = System.currentTimeMillis()
                )
                guardarHabitoEnFirebase(habit)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            }
        }

        // Cancelar
        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun guardarHabitoEnFirebase(habit: Habit) {
        val database = FirebaseDatabase.getInstance()
        val habitsRef = database.getReference("habits")

        if (habitId == null) {
            // Crear un nuevo hábito
            val newHabitId = habitsRef.push().key // Genera un ID único
            if (newHabitId != null) {
                habitsRef.child(newHabitId).setValue(habit)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Hábito añadido con éxito", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error al guardar el hábito", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
            }
        } else {
            // Actualizar un hábito existente
            habitsRef.child(habitId!!).setValue(habit)
                .addOnSuccessListener {
                    Toast.makeText(this, "Hábito actualizado con éxito", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al actualizar el hábito", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
        }
    }

    private fun cargarDatosDelHabito(habitId: String, nameInput: EditText, descriptionInput: EditText, frequencyInput: EditText, categorySpinner: Spinner) {
        val database = FirebaseDatabase.getInstance()
        val habitRef = database.getReference("habits").child(habitId)

        habitRef.get().addOnSuccessListener { dataSnapshot ->
            val habit = dataSnapshot.getValue(Habit::class.java)
            if (habit != null) {
                nameInput.setText(habit.name)
                descriptionInput.setText(habit.description)
                frequencyInput.setText(habit.frequency.toString())
                val categoryIndex = (categorySpinner.adapter as ArrayAdapter<String>).getPosition(habit.category)
                categorySpinner.setSelection(categoryIndex)
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Error al cargar el hábito", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}
