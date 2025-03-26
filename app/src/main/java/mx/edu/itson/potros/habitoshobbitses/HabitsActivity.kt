package mx.edu.itson.potros.habitoshobbitses

import mx.edu.itson.potros.habitoshobbitses.Habit
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.database.*
class HabitsActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var barChart: BarChart
    private lateinit var addHabitButton: Button
    private lateinit var viewProgressButton: Button // Declarar el botón de progreso
    private lateinit var databaseReference: DatabaseReference
    private lateinit var habitList: MutableList<Habit>
    private lateinit var habitAdapter: HabitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habits)

        // Referencias a los componentes
        listView = findViewById(R.id.lv_habits)
        barChart = findViewById(R.id.bar_chart)
        addHabitButton = findViewById(R.id.btn_add_habit)
        viewProgressButton = findViewById(R.id.btn_view_progress) // Vincular el botón

        // Inicializar Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("habits")
        habitList = mutableListOf()

        // Configurar adaptador para el ListView
        habitAdapter = HabitAdapter(this, habitList, databaseReference)
        listView.adapter = habitAdapter

        // Configurar botón para añadir nuevos hábitos
        addHabitButton.setOnClickListener {
            val intent = Intent(this, FormHabitActivity::class.java)
            startActivity(intent)
        }

        // Configurar botón para ver progreso
        viewProgressButton.setOnClickListener {
            val intent = Intent(this, ProgressActivity::class.java)
            startActivity(intent)
        }

        // Configurar lista para cambiar el estado de completado
        listView.setOnItemClickListener { _, _, position, _ ->
            val habit = habitList[position]
            habit.completed = !habit.completed // Cambiar estado de completado
            actualizarEstadoHabit(habit)
            cargarGraficaDiaria() // Actualizar gráfica
        }

        // Cargar hábitos desde Firebase
        cargarHabitosDesdeFirebase()
    }

    private fun cargarHabitosDesdeFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                habitList.clear()
                for (habitSnapshot in snapshot.children) {
                    val habit = habitSnapshot.getValue(Habit::class.java)
                    habit?.let { habitList.add(it) }
                }
                habitAdapter.notifyDataSetChanged()
                cargarGraficaDiaria()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HabitsActivity, "Error al cargar los hábitos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun actualizarEstadoHabit(habit: Habit) {
        if (habit.completed) {
            habit.streak += 1 // Incrementa la racha
        } else {
            habit.streak = 0 // Reinicia la racha
        }
        if (habit.streak == 7) {
            Toast.makeText(this, "¡Felicidades! Has alcanzado una racha de 7 días.", Toast.LENGTH_LONG).show()
        }

        databaseReference.child(habit.id).setValue(habit)
            .addOnFailureListener {
                Toast.makeText(this, "Error al actualizar el hábito", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cargarGraficaDiaria() {
        val completedCount = habitList.count { it.completed }
        val pendingCount = habitList.size - completedCount

        val entries = listOf(
            BarEntry(1f, completedCount.toFloat()),
            BarEntry(2f, pendingCount.toFloat())
        )

        val dataSet = BarDataSet(entries, "Estado de Hábitos")
        val barData = BarData(dataSet)

        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.invalidate() // Refrescar gráfico
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_habits, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.order_by_name -> habitList.sortBy { it.name }
            R.id.order_by_category -> habitList.sortBy { it.category }
            R.id.order_by_status -> habitList.sortByDescending { it.completed }
        }
        habitAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Ordenado correctamente", Toast.LENGTH_SHORT).show()
        return super.onOptionsItemSelected(item)
    }


}
