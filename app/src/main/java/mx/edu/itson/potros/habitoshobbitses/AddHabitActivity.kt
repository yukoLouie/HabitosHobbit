package mx.edu.itson.potros.habitoshobbitses

import android.R
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class AddHabitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_habit)

        val btnGuardar = findViewById<Button>(R.id.btn_guardar)
        val btnCancelar = findViewById<Button>(R.id.btn_cancelar)

        // Guardar hábito
        btnGuardar.setOnClickListener { v: View? -> }

        // Cancelar
        btnCancelar.setOnClickListener { v: View? -> finish() }
    }
}
