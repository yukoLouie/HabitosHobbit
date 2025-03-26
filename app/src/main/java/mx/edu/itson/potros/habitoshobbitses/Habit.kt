package mx.edu.itson.potros.habitoshobbitses

data class Habit(
    val id: String = "",          // Unique identifier for the habit
    val name: String = "",        // Name of the habit
    val description: String = "", // Description of the habit
    val frequency: Int = 0,       // Weekly frequency
    val reminderTime: String = "", // Time for the reminder in "HH:mm" format
    val category: String = "",    // Category of the habit
    val icon: String = "",        // Optional icon for the habit
    var streak: Int = 0,          // Streak for consecutive completions
    var completed: Boolean = false, // Whether the habit is completed today
    val timestamp: Long = 0L      // Timestamp for creation
)