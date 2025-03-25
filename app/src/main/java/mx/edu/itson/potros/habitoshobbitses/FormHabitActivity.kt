package mx.edu.itson.potros.habitoshobbitses


import mx.edu.itson.potros.habitoshobbitses.R
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FormHabitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_habit)

        val nameInput = findViewById<EditText>(R.id.et_habit_name)
        val descriptionInput = findViewById<EditText>(R.id.et_habit_description)
        val frequencyInput = findViewById<EditText>(R.id.et_habit_frequency)
        val categorySpinner = findViewById<Spinner>(R.id.spinner_category)
        val saveButton = findViewById<Button>(R.id.btn_save_habit)
        val cancelButton = findViewById<Button>(R.id.btn_cancel_habit)

        saveButton.setOnClickListener {
            val name = nameInput.text.toString()
            val description = descriptionInput.text.toString()
            val frequency = frequencyInput.text.toString()

            if (name.isNotEmpty() && frequency.isNotEmpty()) {
                // Guardar hábito en base de datos
                Toast.makeText(this, "Hábito guardado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Por favor, llena todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }
}
