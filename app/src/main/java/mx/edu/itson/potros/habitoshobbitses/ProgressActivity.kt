package mx.edu.itson.potros.habitoshobbitses

import android.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class ProgressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)

        val barChart: BarChart = findViewById(R.id.bar_chart)

        // Datos ficticios para la gráfica
        val entries: MutableList<BarEntry> = ArrayList<BarEntry>()
        entries.add(BarEntry(1, 3)) // Día 1: 3 hábitos completados
        entries.add(BarEntry(2, 5)) // Día 2: 5 hábitos completados

        val dataSet: BarDataSet = BarDataSet(entries, "Hábitos Completados")
        val barData: BarData = BarData(dataSet)
        barChart.setData(barData)
        barChart.invalidate()
    }
}
