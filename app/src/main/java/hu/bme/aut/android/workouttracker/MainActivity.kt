package hu.bme.aut.android.workouttracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import hu.bme.aut.android.workouttracker.adapter.FinishedExerciseAdapter
import hu.bme.aut.android.workouttracker.data.FinishedExercise
import hu.bme.aut.android.workouttracker.data.FinishedExercisesDatabase
import hu.bme.aut.android.workouttracker.model.NewExerciseDialogFragment
import hu.bme.aut.android.workouttracker.model.SearchDayDialogFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(), NewExerciseDialogFragment.NewExerciseDialogListener,
    FinishedExerciseAdapter.DeleteExerciseClickListener
//    SearchDayDialogFragment.SearchDayDialogListener
        //Home.SearchHomeListener
{
    private lateinit var database: FinishedExercisesDatabase
    private lateinit var adapter: FinishedExerciseAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = FinishedExercisesDatabase.getDatabase(applicationContext)
        adapter = FinishedExerciseAdapter(this)
        Log.d("MainActivity",adapter.toString())
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = Navigation.findNavController(this, R.id.fragmentContainerView2)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.home2, R.id.add_exercise, R.id.copy, R.id.search_day))
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onExerciseDeleted(exercise: FinishedExercise){
        thread{
            database.FinishedExerciseDao().deleteItem(exercise)
            Log.d("MainActivity"," ShoppingItem delete was successful")

            runOnUiThread {
                adapter.deleteExercise(exercise)
            }
        }
    }

    override fun onExerciseCreated(newExercise: FinishedExercise) {
        thread {
            database.FinishedExerciseDao().insert(newExercise)
            Log.d("MainActivity", "onExerciseCreated")
            runOnUiThread {
                adapter.addExercise(newExercise)
            }
        }
    }

//    override fun onSearchDay(day: Calendar) {
//        thread {
//            val items = database.FinishedExerciseDao().getExercisesByDate(day.startOfDay(), day.endOfDay())
//            Log.d("FromMain","loadItemsInBackground")
//            runOnUiThread {
//                //binding.btDate.text = day.toFormattedDate()
//                adapter.update(items)
//            }
//        }
//    }

    private fun Calendar.toFormattedDate(): String{
        return when{
            DateUtils.isToday(this.timeInMillis + DateUtils.DAY_IN_MILLIS) -> "YESTERDAY"
            DateUtils.isToday(this.timeInMillis - DateUtils.DAY_IN_MILLIS) -> "TOMORROW"
            DateUtils.isToday(this.timeInMillis) -> "TODAY"
            else -> SimpleDateFormat("MM/dd/yyyy").format(this.time)
        }

    }

    private fun Calendar.startOfDay(): Calendar{
        this.set(Calendar.HOUR,0)
        this.set(Calendar.MINUTE,0)
        return this
    }

    private fun Calendar.endOfDay(): Calendar{
        this.set(Calendar.HOUR,23)
        this.set(Calendar.MINUTE,59)
        return this
    }




}