package mx.edu.itson.potros.habitoshobbitses

import mx.edu.itson.potros.habitoshobbitses.Habit
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import android.widget.*

class FormHabitActivity : AppCompatActivity() {

    private var habitId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_habit)

        // Referencias a los elementos de la interfaz de usuario
        val nameInput = findViewById<EditText>(R.id.et_habit_name)
        val descriptionInput = findViewById<EditText>(R.id.et_habit_description)
        val frequencyInput = findViewById<EditText>(R.id.et_habit_frequency)
        val reminderTimeInput = findViewById<EditText>(R.id.et_reminder_time)
        val categorySpinner = findViewById<Spinner>(R.id.spinner_category)
        val newCategoryInput = findViewById<EditText>(R.id.et_new_category)
        val iconoColorTextView = findViewById<TextView>(R.id.tv_icono_color)
        val saveButton = findViewById<Button>(R.id.btn_save_habit)
        val cancelButton = findViewById<Button>(R.id.btn_cancel_habit)

        // Configuración del spinner de categorías
        val categories = mutableListOf("Personal", "Salud", "Productividad")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        // Selector de ícono/color
        iconoColorTextView.setOnClickListener {
            val options = arrayOf("Rojo", "Azul", "Verde", "Amarillo")
            AlertDialog.Builder(this)
                .setTitle("Selecciona un Ícono o Color")
                .setItems(options) { _, which ->
                    iconoColorTextView.text = "Seleccionado: ${options[which]}"
                }
                .show()
        }

        // Configurar el campo de hora de recordatorio
        reminderTimeInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(this, { _, hourOfDay, minute ->
                val time = String.format("%02d:%02d", hourOfDay, minute)
                reminderTimeInput.setText(time)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        // Botón Cancelar
        cancelButton.setOnClickListener { finish() }

        // Configurar si estamos editando un hábito existente
        habitId = intent.getStringExtra("habit_id")
        if (habitId != null) {
            cargarDatosDelHabito(
                habitId!!,
                nameInput,
                descriptionInput,
                frequencyInput,
                reminderTimeInput,
                categorySpinner
            )
        }

        // Botón Guardar
        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()
            val frequency = frequencyInput.text.toString().toIntOrNull()
            val reminderTime = reminderTimeInput.text.toString().trim()
            val category = newCategoryInput.text.toString().takeIf { it.isNotEmpty() }
                ?: categorySpinner.selectedItem.toString()
            val iconOrColor = iconoColorTextView.text.toString()

            if (name.isNotEmpty() && frequency != null) {
                val habit = Habit(
                    name = name,
                    description = description,
                    frequency = frequency,
                    reminderTime = reminderTime,
                    category = category,
                    icon = iconOrColor,
                    completed = false,
                    timestamp = System.currentTimeMillis()
                )
                guardarHabitoEnFirebase(habit)
            } else {
                Toast.makeText(
                    this,
                    "Por favor, completa los campos obligatorios",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun guardarHabitoEnFirebase(habit: Habit) {
        val database = FirebaseDatabase.getInstance().getReference("habits")
        if (habitId == null) {
            // Generar un nuevo ID para el hábito
            val habitKey = database.push().key ?: return
            val newHabit = habit.copy(id = habitKey) // Copiar el hábito y asignar el ID
            database.child(habitKey).setValue(newHabit)
                .addOnSuccessListener {
                    Toast.makeText(this, "Hábito guardado correctamente", Toast.LENGTH_SHORT).show()
                    regresarAActivityHabits()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al guardar el hábito: ${e.message}", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
        } else {
            // Actualizar un hábito existente
            database.child(habitId!!).setValue(habit)
                .addOnSuccessListener {
                    Toast.makeText(this, "Hábito actualizado correctamente", Toast.LENGTH_SHORT).show()
                    regresarAActivityHabits()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al actualizar el hábito: ${e.message}", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
        }
    }


    private fun regresarAActivityHabits() {
        startActivity(Intent(this, HabitsActivity::class.java))
        finish()
    }

    private fun cargarDatosDelHabito(
        habitId: String,
        nameInput: EditText,
        descriptionInput: EditText,
        frequencyInput: EditText,
        reminderTimeInput: EditText,
        categorySpinner: Spinner
    ) {
        FirebaseDatabase.getInstance().getReference("habits").child(habitId).get()
            .addOnSuccessListener { snapshot ->
                snapshot.getValue(Habit::class.java)?.let { habit ->
                    nameInput.setText(habit.name)
                    descriptionInput.setText(habit.description)
                    frequencyInput.setText(habit.frequency.toString())
                    reminderTimeInput.setText(habit.reminderTime)
                    val index =
                        (categorySpinner.adapter as ArrayAdapter<String>).getPosition(habit.category)
                    categorySpinner.setSelection(index)
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar el hábito: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                e.printStackTrace()
            }
    }
}

/*
habitId = intent.getStringExtra("habit_id")
if (habitId != null) {
    cargarDatosDelHabito(habitId!!, nameInput, descriptionInput, frequencyInput, categorySpinner)
}

iconoColorTextView.setOnClickListener {
    val options = arrayOf("Rojo", "Azul", "Verde")
    AlertDialog.Builder(this)
        .setTitle("Selecciona un ícono o color")
        .setItems(options) { _, which ->
            iconoColorTextView.text = "Ícono/Color: ${options[which]}"
        }
        .show()
}

//timePicker.setOnClickListener {
    val calendar = Calendar.getInstance()
    TimePickerDialog(
        this,
        { _, hourOfDay, minute ->
    //        timePicker.setText(String.format("%02d:%02d", hourOfDay, minute))
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    ).show()
}

// saveButton.setOnClickListener {
 //   val name = nameInput.text.toString()
   // val description = descriptionInput.text.toString()
//     val frequency = frequencyInput.text.toString().toIntOrNull()
//    val category = categorySpinner.selectedItem.toString()
//    val icon = "default"

//       if (name.isNotEmpty() && frequency != null) {
//         val habit = Habit(
//            name, description, frequency, category, icon, false, System.currentTimeMillis()
//         )
//          guardarHabitoEnFirebase(habit)
//       } else {
//           Toast.makeText(this, "Por favor, completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
//        }
//   }

//  cancelButton.setOnClickListener {
//       finish()
//   }
// }

private fun guardarHabitoEnFirebase(habit: Habit) {
val database = FirebaseDatabase.getInstance()
val habitsRef = database.getReference("habits")

if (habitId == null) {
    val newHabitId = habitsRef.push().key
    newHabitId?.let {
        habitsRef.child(it).setValue(habit)
            .addOnSuccessListener {
                Toast.makeText(this, "Hábito añadido con éxito", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar el hábito", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
    }
} else {
    habitsRef.child(habitId!!).setValue(habit)
        .addOnSuccessListener {
            Toast.makeText(this, "Hábito actualizado con éxito", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Error al actualizar el hábito", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
}
}

private fun cargarDatosDelHabito(
habitId: String,
nameInput: EditText,
descriptionInput: EditText,
frequencyInput: EditText,
categorySpinner: Spinner
) {
val database = FirebaseDatabase.getInstance()
val habitRef = database.getReference("habits").child(habitId)

habitRef.get().addOnSuccessListener { snapshot ->
    snapshot.getValue(Habit::class.java)?.let { habit ->
        nameInput.setText(habit.name)
        // descriptionInput.setText(habit.description)
        frequencyInput.setText(habit.frequency.toString())
        val index = (categorySpinner.adapter as ArrayAdapter<String>).getPosition(habit.category)
        categorySpinner.setSelection(index)
    }
}.addOnFailureListener { e ->
    Toast.makeText(this, "Error al cargar el hábito", Toast.LENGTH_SHORT).show()
    e.printStackTrace()
}*/


