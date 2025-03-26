package mx.edu.itson.potros.habitoshobbitses

data class Habit(
    val name: String,
    val icon: Int, // Recurso de ícono o color
    val category: String,
    val frequency: Int, // Frecuencia semanal
    var completedToday: Boolean // Estado actual
)
