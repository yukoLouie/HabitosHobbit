package mx.edu.itson.potros.habitoshobbitses


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.lv_habits)
        val barChart = findViewById<BarChart>(R.id.bar_chart_daily)
        val addHabitButton = findViewById<Button>(R.id.btn_add_habit)

        // Lógica para cargar hábitos en ListView
        cargarHabitos(listView)

        // Lógica para cargar datos en la gráfica
        cargarGrafica(barChart)

        addHabitButton.setOnClickListener {
            startActivity(Intent(this, FormHabitActivity::class.java))
        }
    }

    private fun cargarHabitos(listView: ListView) {
        // Lógica para obtener hábitos desde una base de datos o lista estática
    }

    private fun cargarGrafica(barChart: BarChart) {
        // Lógica para graficar hábitos completados
    }
}
