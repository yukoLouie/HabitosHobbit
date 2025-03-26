package mx.edu.itson.potros.habitoshobbitses

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.util.Calendar

class ProgressActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)

        dbHelper = DatabaseHelper(this) // Inicializar dbHelper

        val pieChart = findViewById<PieChart>(R.id.pie_chart_weekly)
        val barChartMonthly = findViewById<BarChart>(R.id.bar_chart_monthly)
        val filterButton = findViewById<Button>(R.id.btn_filter_progress)

        cargarGraficaSemanal(pieChart, null) // Inicialmente, muestra todos los datos
        cargarGraficaMensual(barChartMonthly)

        filterButton.setOnClickListener {
            mostrarFiltro(pieChart)
        }
    }

    private fun cargarGraficaSemanal(pieChart: PieChart, categoria: String?) {
        val habits = dbHelper.getAllHabits(this)
        val filteredHabits = if (categoria != null) {
            habits.filter { it.category == categoria }
        } else {
            habits
        }

        val entries = filteredHabits.groupBy { it.category }.map { (category, habitList) ->
            PieEntry(habitList.size.toFloat(), category)
        }

        val dataSet = PieDataSet(entries, "Cumplimiento Semanal")
        dataSet.colors = listOf(getColor(R.color.colorSalud), getColor(R.color.colorPersonal), getColor(R.color.colorProductividad))
        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.invalidate()
    }



    private fun cargarGraficaMensual(barChart: BarChart) {
        val habits = dbHelper.getAllHabits(this)
        val weeklyData = habits.groupBy { it.frequency / 7 } // Agrupar por semanas

        val entries = weeklyData.map { (week, habitList) ->
            BarEntry(week.toFloat(), habitList.size.toFloat())
        }

        val dataSet = BarDataSet(entries, "Cumplimiento Mensual")
        dataSet.color = getColor(R.color.primaryColor)
        val barData = BarData(dataSet)
        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.invalidate()
    }



    private fun mostrarFiltro(pieChart: PieChart) {
        val options = arrayOf("Salud", "Personal", "Productividad", "Todos")
        AlertDialog.Builder(this)
            .setTitle("Filtrar por categoría")
            .setItems(options) { _, which ->
                val selectedCategory = if (options[which] == "Todos") null else options[which]
                Toast.makeText(this, "Filtrando por: ${options[which]}", Toast.LENGTH_SHORT).show()
                cargarGraficaSemanal(pieChart, selectedCategory)
            }
            .show()
    }
    private fun mostrarFiltroPorFecha(barChart: BarChart) {
        val calendar = Calendar.getInstance()

        // Selección de fecha inicial
        DatePickerDialog(this, { _, startYear, startMonth, startDay ->
            val startDate = Calendar.getInstance()
            startDate.set(startYear, startMonth, startDay)

            // Selección de fecha final
            DatePickerDialog(this, { _, endYear, endMonth, endDay ->
                val endDate = Calendar.getInstance()
                endDate.set(endYear, endMonth, endDay)

                // Filtrar datos por rango de fechas
                val habits = dbHelper.getAllHabits(this).filter { habit ->
                    // Aquí puedes verificar que el hábito esté dentro del rango de fechas
                    true // Lógica de filtrado por fechas
                }

                // Actualizar gráfica con los datos filtrados
                cargarGraficaMensual(barChart)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }


}
