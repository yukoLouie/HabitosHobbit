package mx.edu.itson.potros.habitoshobbitses

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "HabitsDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE Habits (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "description TEXT, " +
                    "frequency INTEGER, " +
                    "category TEXT, " +
                    "icon TEXT, " +
                    "completedToday INTEGER)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Habits")
        onCreate(db)
    }
}
/*
    fun insertHabit(
        name: String,
        description: String,
        frequency: Int,
        category: String,
        icon: String,
        completedToday: Int
    ) {
        val db = writableDatabase
        db.execSQL(
            "INSERT INTO Habits (name, description, frequency, category, icon, completedToday) VALUES (?, ?, ?, ?, ?, ?)",
            arrayOf(name, description, frequency, category, icon, completedToday)
        )
    }
}
*/
// fun getAllHabits(context: Context): MutableList<Habit> {
// val db = readableDatabase
//  val cursor = db.rawQuery("SELECT * FROM Habits", null)
// val habits = mutableListOf<Habit>()

        //  while (cursor.moveToNext()) {
            // val name = cursor.getString(cursor.getColumnIndex("name")) ?: ""
            // val description = cursor.getString(cursor.getColumnIndex("description")) ?: ""
            //// val frequency = cursor.getInt(cursor.getColumnIndex("frequency")).takeIf { it >= 0 } ?: 0
            //val category = cursor.getString(cursor.getColumnIndex("category")) ?: ""
            // val completedToday = cursor.getInt(cursor.getColumnIndex("completedToday")) == 1
            // val iconResId = cursor.getString(cursor.getColumnIndex("icon"))?.let { resourceName ->
            //     context.resources.getIdentifier(resourceName, "drawable", context.packageName)
            //  } ?: R.drawable.ic_logo // Usa un ícono válido si no se encuentra

        //  val habit = Habit(name, iconResId, category, frequency, completedToday)
        //   habits.add(habit)
        //  }
    // cursor.close()
    // return habits
        // }
//fun updateHabitStatus(id: Int, completedToday: Boolean) {
//  val db = writableDatabase
// val status = if (completedToday) 1 else 0
//db.execSQL(
// "UPDATE Habits SET completedToday = ? WHERE id = ?",
//arrayOf(status, id)
//)
// }
    //   fun getHabitById(id: Int): Habit {
        //  val db = readableDatabase
        //  val cursor = db.rawQuery("SELECT * FROM Habits WHERE id = ?", arrayOf(id.toString()))
        //  cursor.moveToFirst()
        //  val habit = Habit(
            //    cursor.getString(cursor.getColumnIndex("name")),
            //    R.drawable.ic_logo, // Sustituir con lógica de ícono real
            //     cursor.getString(cursor.getColumnIndex("category")),
            //    cursor.getInt(cursor.getColumnIndex("frequency")),
            //      cursor.getInt(cursor.getColumnIndex("completedToday")) == 1
            //  )
        //   cursor.close()
        //   return habit
        //}

//fun updateHabit(id: Int, name: String, description: String, frequency: Int, category: String, icon: String) {
//  val db = writableDatabase
//  db.execSQL(
//  "UPDATE Habits SET name = ?, description = ?, frequency = ?, category = ?, icon = ? WHERE id = ?",
//  arrayOf(name, description, frequency, category, icon, id)
//  )
//}



//}
