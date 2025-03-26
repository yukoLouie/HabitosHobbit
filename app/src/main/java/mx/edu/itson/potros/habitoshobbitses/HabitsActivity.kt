package mx.edu.itson.potros.habitoshobbitses

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class HabitsActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habits)

        // Inicializar la base de datos
        dbHelper = DatabaseHelper(this)
        // Inserta datos de prueba (solo para desarrollo, eliminar luego)
        insertarDatosDePrueba()
        // Referencias al ListView y al BarChart
        val listView = findViewById<ListView>(R.id.lv_habits)
        val barChart = findViewById<BarChart>(R.id.bar_chart)
        val addHabitButton = findViewById<Button>(R.id.btn_add_habit)

        // Cargar hábitos desde la base de datos
        val habits = dbHelper.getAllHabits(this) // Proporciona el contexto

        // Configurar el adaptador para el ListView
        listView.adapter = HabitAdapter(this, habits)

        // Configurar la gráfica diaria
        cargarGraficaDiaria(barChart, habits)

        // Configurar el botón para añadir nuevos hábitos
        addHabitButton.setOnClickListener {
            val intent = Intent(this, FormHabitActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cargarGraficaDiaria(barChart: BarChart, habits: List<Habit>) {
        // Filtrar hábitos completados
        val completedCount = habits.count { it.completedToday }
        val pendingCount = habits.size - completedCount

        // Crear entradas
        val entries = listOf(
            BarEntry(1f, completedCount.toFloat()), // Completados
            BarEntry(2f, pendingCount.toFloat())   // Pendientes
        )

        // Configurar DataSet
        val dataSet = BarDataSet(entries, "Hábitos del Día")
        dataSet.colors = listOf(getColor(R.color.colorSalud), getColor(R.color.colorProductividad))
        val barData = BarData(dataSet)

        // Mostrar la gráfica
        barChart.data = barData
        barChart.description.isEnabled = false // Desactivar la descripción
        barChart.invalidate() // Refrescar la gráfica
    }
    private fun insertarDatosDePrueba() {
        val dbHelper = DatabaseHelper(this)

        dbHelper.insertHabit("Ejercicio", "30 minutos de cardio", 7, "Salud", "default", 0, System.currentTimeMillis())
        dbHelper.insertHabit("Leer", "Leer un capítulo de libro", 5, "Personal", "default", 0, System.currentTimeMillis())
        dbHelper.insertHabit("Meditación", "10 minutos de meditación", 3, "Productividad", "default", 0, System.currentTimeMillis())
    }

}
