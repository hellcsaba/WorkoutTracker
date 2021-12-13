package hu.bme.aut.android.workouttracker.data

import androidx.room.*
import java.util.*

@Dao
interface FinishedExerciseDao {
    @Query("SELECT * FROM finishedExercise")
    fun getAll(): List<FinishedExercise>

    @Insert
    fun insert(exercise: FinishedExercise): Long

    @Update
    fun update(exercise: FinishedExercise)

    @Delete
    fun deleteItem(exercise: FinishedExercise)

    @Query("SELECT * FROM finishedExercise WHERE date BETWEEN :from AND :to")
    fun getExercisesByDate(from: Calendar, to: Calendar): List<FinishedExercise>
}