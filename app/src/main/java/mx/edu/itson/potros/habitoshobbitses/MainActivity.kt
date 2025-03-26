package mx.edu.itson.potros.habitoshobbitses

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Elementos del layout
        val btnIngresar = findViewById<Button>(R.id.btn_ingresar)
        val btnRegistrar = findViewById<Button>(R.id.btn_registrar)
        val descripcion = findViewById<TextView>(R.id.descripcion)

        // Configuración inicial del texto de descripción o eslogan
        descripcion.text = getString(R.string.slogan) // Actualiza el texto si es necesario

        // Navegación al iniciar sesión
        btnIngresar.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Navegación al registro
        btnRegistrar.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}
