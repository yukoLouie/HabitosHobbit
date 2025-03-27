package mx.edu.itson.potros.habitoshobbitses

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class FormHabitActivity : AppCompatActivity() {

    private var habitId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_habit)

        // UI references
        val nameInput = findViewById<EditText>(R.id.et_habit_name)
        val descriptionInput = findViewById<EditText>(R.id.et_habit_description)
        // We'll repurpose this field so the user can choose days.
        val frequencyInput = findViewById<EditText>(R.id.et_habit_frequency)
        // Make frequencyInput non-editable so it acts as a chooser.
        frequencyInput.isFocusable = false
        frequencyInput.isClickable = true

        val reminderTimeInput = findViewById<EditText>(R.id.et_reminder_time)
        val categorySpinner = findViewById<Spinner>(R.id.spinner_category)
        val newCategoryInput = findViewById<EditText>(R.id.et_new_category)
        val iconoColorTextView = findViewById<TextView>(R.id.tv_icono_color)
        val saveButton = findViewById<Button>(R.id.btn_save_habit)
        val cancelButton = findViewById<Button>(R.id.btn_cancel_habit)

        // Configure category spinner with default categories
        val categories = mutableListOf("Personal", "Salud", "Productividad")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = spinnerAdapter

        // Set up the icon/color selector
        iconoColorTextView.setOnClickListener {
            val options = arrayOf("Rojo", "Azul", "Verde", "Amarillo")
            AlertDialog.Builder(this)
                .setTitle("Selecciona un Ícono o Color")
                .setItems(options) { _, which ->
                    iconoColorTextView.text = "Seleccionado: ${options[which]}"
                }
                .show()
        }

        // Set up the reminder time chooser
        reminderTimeInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(this, { _, hourOfDay, minute ->
                val time = String.format("%02d:%02d", hourOfDay, minute)
                reminderTimeInput.setText(time)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        // Set up the frequency (days-of-week) chooser using a multi-choice dialog
        frequencyInput.setOnClickListener {
            val dayOptions = arrayOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
            // Create an array to keep track of checked items. Initially all false.
            val checkedItems = BooleanArray(dayOptions.size) { false }
            AlertDialog.Builder(this)
                .setTitle("Selecciona los días de la semana")
                .setMultiChoiceItems(dayOptions, checkedItems) { _, which, isChecked ->
                    checkedItems[which] = isChecked
                }
                .setPositiveButton("Aceptar") { dialog, _ ->
                    // Create a list of selected day names.
                    val selectedDays = mutableListOf<String>()
                    for (i in dayOptions.indices) {
                        if (checkedItems[i]) {
                            selectedDays.add(dayOptions[i])
                        }
                    }
                    // Update the frequency field text with selected day names separated by commas.
                    frequencyInput.setText(selectedDays.joinToString(", "))
                    dialog.dismiss()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        // Cancel button closes this activity.
        cancelButton.setOnClickListener { finish() }

        // If editing an existing habit, load its data.
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

        // Save button saves the habit.
        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()
            // The frequencyInput now holds the selected days (e.g., "Lunes, Martes, Sábado")
            val selectedDaysText = frequencyInput.text.toString().trim()
            // Number of days selected:
            val frequency = if (selectedDaysText.isNotEmpty()) selectedDaysText.split(",").size else 0
            val reminderTime = reminderTimeInput.text.toString().trim()
            // If the user has typed a new category, use it; otherwise, use the selected one.
            val category = newCategoryInput.text.toString().takeIf { it.isNotEmpty() }
                ?: categorySpinner.selectedItem.toString()
            val iconOrColor = iconoColorTextView.text.toString()

            if (name.isNotEmpty() && selectedDaysText.isNotEmpty() && reminderTime.isNotEmpty()) {
                // Create the habit. Assuming your Habit class now includes a 'days' field.
                val habit = Habit(
                    name = name,
                    description = description,
                    frequency = frequency,
                    reminderTime = reminderTime,
                    category = category,
                    icon = iconOrColor,
                    completed = false,
                    timestamp = System.currentTimeMillis()
                    // You may also include the new field 'days':
                    // days = selectedDaysText
                )
                guardarHabitoEnFirebase(habit)
            } else {
                Toast.makeText(this, "Por favor, completa los campos obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun guardarHabitoEnFirebase(habit: Habit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().getReference("habits")
        if (habitId == null) {
            val habitKey = database.push().key ?: return
            // Copy the habit and assign the new ID and userId
            val newHabit = habit.copy(id = habitKey, userId = userId)
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
                    // Here, frequencyInput will display the days string (e.g., "Lunes, Martes")
                    frequencyInput.setText(/* If you have a "days" field, use that; otherwise, derive it */ "")
                    reminderTimeInput.setText(habit.reminderTime)
                    val index =
                        (categorySpinner.adapter as ArrayAdapter<String>).getPosition(habit.category)
                    categorySpinner.setSelection(index)
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar el hábito: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
    }
}
