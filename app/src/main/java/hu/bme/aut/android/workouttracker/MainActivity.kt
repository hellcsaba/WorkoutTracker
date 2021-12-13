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


class MainActivity : AppCompatActivity()//, FinishedExerciseAdapter.DeleteExerciseClickListener
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = Navigation.findNavController(this, R.id.fragmentContainerView2)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.home2, R.id.add_exercise, R.id.copy, R.id.search_day))
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
    }


}