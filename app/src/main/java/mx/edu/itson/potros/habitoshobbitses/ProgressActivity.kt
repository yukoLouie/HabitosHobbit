package mx.edu.itson.potros.habitoshobbitses


import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart

class ProgressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)

        val pieChart = findViewById<PieChart>(R.id.pie_chart_weekly)
        val filterButton = findViewById<Button>(R.id.btn_filter_progress)

        // Lógica para cargar datos en la gráfica
        cargarGrafica(pieChart)

        filterButton.setOnClickListener {
            // Filtro por fecha y categoría
        }
    }

    private fun cargarGrafica(pieChart: PieChart) {
        // Lógica para generar datos dinámicos para la gráfica
    }
}
