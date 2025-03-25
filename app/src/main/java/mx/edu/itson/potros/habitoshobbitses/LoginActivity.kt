package mx.edu.itson.potros.habitoshobbitses

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnIniciarSesion = findViewById<Button>(R.id.btn_iniciar_sesion)
        val tvRegistrar = findViewById<TextView>(R.id.tv_registrar)

        // Lógica del botón
        btnIniciarSesion.setOnClickListener { v: View? -> }

        // Navegación hacia el registro
        tvRegistrar.setOnClickListener { v: View? ->
            val intent = Intent(
                this@LoginActivity,
                RegisterActivity::class.java
            )
            startActivity(intent)
        }
    }
}
