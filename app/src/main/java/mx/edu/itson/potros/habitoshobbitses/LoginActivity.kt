package mx.edu.itson.potros.habitoshobbitses

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
/*
        val emailInput = findViewById<EditText>(R.id.et_correo)
        val passwordInput = findViewById<EditText>(R.id.et_contraseña)
        val loginButton = findViewById<Button>(R.id.btn_iniciar_sesion)
        val registerLink = findViewById<TextView>(R.id.tv_registrar)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Validar credenciales (aquí se usaría la base de datos)
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        registerLink.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }*/  val emailInput = findViewById<EditText>(R.id.et_correo)
        val passwordInput = findViewById<EditText>(R.id.et_contraseña)
        val loginButton = findViewById<Button>(R.id.btn_iniciar_sesion)
        val registerLink = findViewById<Button>(R.id.tv_registrar)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Validación ficticia de credenciales
                if (email == "usuario@test.com" && password == "123456") {
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    // Redirigir a ActivityHabits
                    val intent = Intent(this, HabitsActivity::class.java)
                    startActivity(intent)
                    finish() // Finalizar LoginActivity
                } else {
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        registerLink.setOnClickListener {
            // Redirigir a la actividad de registro
            startActivity(Intent(this, Register::class.java))
        }
    }
    }

