package mx.edu.itson.potros.habitoshobbitses



import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        /*
        val nameInput = findViewById<EditText>(R.id.et_name)
        val emailInput = findViewById<EditText>(R.id.et_email)
        val passwordInput = findViewById<EditText>(R.id.et_password)
        val confirmPasswordInput = findViewById<EditText>(R.id.et_confirm_password)
        val registerButton = findViewById<Button>(R.id.btn_register)
        val loginLink = findViewById<TextView>(R.id.tv_login_link)

        registerButton.setOnClickListener {
            val name = nameInput.text.toString()
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    // Guardar usuario en base de datos
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }*/
// Referencias a los elementos de la UI
        val nameInput = findViewById<EditText>(R.id.et_name)
        val emailInput = findViewById<EditText>(R.id.et_email)
        val passwordInput = findViewById<EditText>(R.id.et_password)
        val confirmPasswordInput = findViewById<EditText>(R.id.et_confirm_password)
        val registerButton = findViewById<Button>(R.id.btn_register)
        val loginLink = findViewById<Button>(R.id.tv_login_link)

        // Acción para registrarse
        registerButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    // Simulación de registro exitoso
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HabitsActivity::class.java)
                    startActivity(intent)
                    finish() // Finalizar la actividad de registro
                } else {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Acción para regresar al login
        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Finaliza la actividad de registro
        }
    }

}
