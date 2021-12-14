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

    @Query("SELECT category FROM exercise WHERE exercise.name == :name")
    fun getCategoryByName(name: String): Exercise.Category

    @Query("SELECT * FROM exercise WHERE exercise.name == :name")
    fun getExerciseByName(name: String): Exercise?

    @Query("SELECT exercise.name FROM exercise")
    fun getExerciseNames(): List<String>
}