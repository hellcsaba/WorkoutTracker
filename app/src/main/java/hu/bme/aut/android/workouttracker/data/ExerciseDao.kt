package hu.bme.aut.android.workouttracker.data

import androidx.room.*

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercise")
    fun getAll(): List<Exercise>

    @Insert
    fun insert(exercise: Exercise): Long

    @Update
    fun update(exercise: Exercise)

    @Delete
    fun deleteItem(exercise: Exercise)
}