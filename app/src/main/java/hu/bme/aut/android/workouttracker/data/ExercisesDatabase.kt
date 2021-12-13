package hu.bme.aut.android.workouttracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

//@Database(entities = [Exercise::class], version = 1)
//@TypeConverters(value = [Exercise.Category::class])
//abstract class ExercisesDatabase:RoomDatabase() {
//    abstract fun ExercisesDatabase(): ExercisesDatabase
//
//    companion object{
//        fun getDatabase(applicationContext: Context): ExercisesDatabase {
//            return Room.databaseBuilder(
//                applicationContext,
//                ExercisesDatabase::class.java,
//                "exercises_db"
//            ).build();
//        }
//    }
//}