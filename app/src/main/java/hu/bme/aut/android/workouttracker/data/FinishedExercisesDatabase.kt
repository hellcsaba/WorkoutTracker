package hu.bme.aut.android.workouttracker.data

import android.content.Context
import androidx.room.*

@Database(entities = [FinishedExercise::class], version = 2)
@TypeConverters(value = arrayOf(FinishedExercise.DateConverter::class, FinishedExercise.Category::class))
//@TypeConverters(value = [FinishedExercise.DateConverter::class])
abstract class FinishedExercisesDatabase: RoomDatabase() {
    abstract fun FinishedExerciseDao(): FinishedExerciseDao

    companion object{
        fun getDatabase(applicationContext: Context): FinishedExercisesDatabase {
            return Room.databaseBuilder(
                applicationContext,
                FinishedExercisesDatabase::class.java,
                "finished_exercises_db"
            ).fallbackToDestructiveMigration()
                .build();
        }
    }
}