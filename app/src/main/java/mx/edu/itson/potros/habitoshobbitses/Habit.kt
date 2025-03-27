package mx.edu.itson.potros.habitoshobbitses

data class Habit(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val description: String = "",
    val frequency: Int = 0,       // Puede ser el número de días seleccionados
    val days: String = "",        // Ejemplo: "Lunes,Martes,Sábado"
    val reminderTime: String = "",// Hora en formato "HH:mm"
    val category: String = "",
    val icon: String = "",
    var streak: Int = 0,
    var completed: Boolean = false,
    val timestamp: Long = 0L
)
