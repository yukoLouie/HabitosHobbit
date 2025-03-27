package mx.edu.itson.potros.habitoshobbitses

import mx.edu.itson.potros.habitoshobbitses.R
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


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
}
