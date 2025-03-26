package mx.edu.itson.potros.habitoshobbitses


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import android.widget.BaseAdapter
import androidx.annotation.LayoutRes
import com.google.firebase.database.DatabaseReference

class HabitAdapter(
    private val context: Context,
    private val habits: List<Habit>,
    private val databaseReference: DatabaseReference
) : BaseAdapter() {

    override fun getCount(): Int = habits.size

    override fun getItem(position: Int): Any = habits[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view =
            convertView ?: LayoutInflater.from(context).inflate(R.layout.item_habit, parent, false)
        val habit = habits[position]

        val nameTextView = view.findViewById<TextView>(R.id.tv_habit_name)
        val categoryTextView = view.findViewById<TextView>(R.id.tv_habit_category)
        val frequencyTextView = view.findViewById<TextView>(R.id.tv_habit_frequency)
        val statusTextView = view.findViewById<TextView>(R.id.tv_habit_status)

        nameTextView.text = habit.name
        categoryTextView.text = habit.category
        frequencyTextView.text = "Frecuencia: ${habit.frequency} días/semana"
        statusTextView.text = if (habit.completed) "Completado" else "Pendiente"

        // Manejar click en el elemento para cambiar el estado
        view.setOnClickListener {
            if (habit.id.isNotEmpty()) { // Asegurarse de que el ID no esté vacío
                habit.completed = !habit.completed // Cambiar estado
                databaseReference.child(habit.id).setValue(habit)
                    .addOnSuccessListener {
                        Toast.makeText(
                            context,
                            "Estado actualizado correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Error al actualizar estado", Toast.LENGTH_SHORT)
                            .show()
                    }
            } else {
                Toast.makeText(context, "ID del hábito inválido", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
/*
override fun getCount(): Int {
    return habits.size
}

override fun getItem(position: Int): Any {
    return habits[position]
}

override fun getItemId(position: Int): Long {
    return position.toLong()
}

override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
    val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_habit, parent, false)

    val habit = habits[position]

    // Elementos de la vista
    val nameTextView = view.findViewById<TextView>(R.id.tv_habit_name)
    val iconImageView = view.findViewById<ImageView>(R.id.iv_habit_icon)
    val categoryTextView = view.findViewById<TextView>(R.id.tv_habit_category)
    val frequencyTextView = view.findViewById<TextView>(R.id.tv_habit_frequency)
    val completedCheckBox = view.findViewById<CheckBox>(R.id.cb_habit_completed)

    // Configuración de los valores
    nameTextView.text = habit.name
    categoryTextView.text = habit.category
    frequencyTextView.text = "${habit.frequency} días/semana"
    completedCheckBox.isChecked = habit.completedToday

    // Establecer ícono/color
    iconImageView.setImageDrawable(ContextCompat.getDrawable(context, habit.icon))

    completedCheckBox.setOnCheckedChangeListener { _, isChecked ->
        habit.completedToday = isChecked
        val status = if (isChecked) "completado" else "pendiente"
        Toast.makeText(context, "${habit.name} marcado como $status", Toast.LENGTH_SHORT).show()

        // Actualizar en la base de datos
        val dbHelper = DatabaseHelper(context)
        //  dbHelper.updateHabitStatus(position.toInt(), isChecked)
    }


    return view
}*/

