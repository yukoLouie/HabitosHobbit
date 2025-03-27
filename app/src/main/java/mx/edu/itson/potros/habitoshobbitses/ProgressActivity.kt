package mx.edu.itson.potros.habitoshobbitses

import mx.edu.itson.potros.habitoshobbitses.Habit

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.google.firebase.database.*
import java.util.Calendar

class ProgressActivity : AppCompatActivity() {

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart
    private lateinit var filterButton: Button
    private lateinit var filterDateButton: Button

    private lateinit var databaseReference: DatabaseReference
    private lateinit var habitList: MutableList<Habit>
    private var filteredHabitList: MutableList<Habit> = mutableListOf() // Store filtered habits

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)

        // Initialize views
        pieChart = findViewById(R.id.pie_chart_weekly)
        barChart = findViewById(R.id.bar_chart_monthly)
        filterButton = findViewById(R.id.btn_filter_progress)
        filterDateButton = findViewById(R.id.btn_filtrar)

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("habits")
        habitList = mutableListOf()

        // Load initial data
        loadHabitsFromFirebase()

        // Set filter button actions
        filterButton.setOnClickListener {
            showCategoryFilterDialog()
        }

        filterDateButton.setOnClickListener {
            showDateFilterDialog()
        }
    }

    private fun loadHabitsFromFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                habitList.clear()
                for (habitSnapshot in snapshot.children) {
                    val habit = habitSnapshot.getValue(Habit::class.java)
                    habit?.let { habitList.add(it) }
                }
                filteredHabitList = habitList.toMutableList() // Initially show all habits
                updateCharts(filteredHabitList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProgressActivity, "Error al cargar los hábitos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateCharts(habits: List<Habit>) {
        updatePieChart(habits)
        updateBarChart(habits)
    }

    private fun updatePieChart(habits: List<Habit>) {
        val completedCount = habits.count { it.completed }
        val pendingCount = habits.size - completedCount

        val entries = listOf(
            PieEntry(completedCount.toFloat(), "Completado"),
            PieEntry(pendingCount.toFloat(), "Pendiente")
        )

        val dataSet = PieDataSet(entries, "Cumplimiento Semanal")
        dataSet.colors = listOf(
            getColor(R.color.green), // Color for completed
            getColor(R.color.red)   // Color for pending
        )

        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.invalidate() // Refresh the chart
    }

    private fun updateBarChart(habits: List<Habit>) {
        // Group habits by weeks
        val weeklyData = habits.groupBy { it.frequency / 7 } // Group by weeks

        val entries = weeklyData.map { (week, habitList) ->
            BarEntry(week.toFloat(), habitList.size.toFloat())
        }

        val dataSet = BarDataSet(entries, "Cumplimiento Mensual")
        dataSet.color = getColor(R.color.primaryColor)
        val barData = BarData(dataSet)

        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.invalidate() // Refresh the chart
    }

    private fun showCategoryFilterDialog() {
        val options = arrayOf("Salud", "Personal", "Productividad", "Todos")
        AlertDialog.Builder(this)
            .setTitle("Filtrar por categoría")
            .setItems(options) { _, which ->
                val selectedCategory = if (options[which] == "Todos") null else options[which]
                filteredHabitList = if (selectedCategory == null) {
                    habitList.toMutableList() // No filter applied
                } else {
                    habitList.filter { it.category == selectedCategory }.toMutableList()
                }
                Toast.makeText(this, "Filtrando por: ${options[which]}", Toast.LENGTH_SHORT).show()
                updateCharts(filteredHabitList)
            }
            .show()
    }

    private fun showDateFilterDialog() {
        val calendar = Calendar.getInstance()

        // Select start date
        DatePickerDialog(this, { _, startYear, startMonth, startDay ->
            val startDate = Calendar.getInstance()
            startDate.set(startYear, startMonth, startDay)

            // Select end date
            DatePickerDialog(this, { _, endYear, endMonth, endDay ->
                val endDate = Calendar.getInstance()
                endDate.set(endYear, endMonth, endDay)

                // Filter habits by date range
                filteredHabitList = habitList.filter { habit ->
                    val habitDate = Calendar.getInstance()
                    habitDate.timeInMillis = habit.timestamp
                    habitDate.after(startDate) && habitDate.before(endDate)
                }.toMutableList()

                Toast.makeText(this, "Gráfica filtrada por fechas", Toast.LENGTH_SHORT).show()
                updateCharts(filteredHabitList)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
}
