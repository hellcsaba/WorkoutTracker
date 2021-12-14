package hu.bme.aut.android.workouttracker.model

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import hu.bme.aut.android.workouttracker.adapter.FinishedExerciseAdapter
import hu.bme.aut.android.workouttracker.data.FinishedExercise
import hu.bme.aut.android.workouttracker.data.FinishedExercisesDatabase
import hu.bme.aut.android.workouttracker.databinding.FragmentCopyBinding
import hu.bme.aut.android.workouttracker.databinding.FragmentHomeBinding
import java.util.*
import kotlin.concurrent.thread

class CopyFragment : Fragment()
{
    interface CopyFragmentListener{
        fun onCopyExercises(from: Calendar, to: Calendar)
    }
    private lateinit var database: FinishedExercisesDatabase
    private lateinit var binding: FragmentCopyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FinishedExercisesDatabase.getDatabase(requireContext().applicationContext)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCopyBinding.inflate(LayoutInflater.from(context))
        binding.btnCopyExercises.setOnClickListener {
            onCopyExercises(binding.dpStartDate.getDate(), binding.dpEndDate.getDate())
        }
        return binding.root
    }

    fun DatePicker.getDate(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        return calendar
    }

    fun onCopyExercises(from: Calendar, to: Calendar) {
        thread{
            val fromExercises = database.FinishedExerciseDao().getExercisesByDate(startOfDay(from), endOfDay(from))
            if(!fromExercises.isEmpty()){
                for(exercise in fromExercises){
                    val ex = FinishedExercise(
                        name = exercise.name,
                        reps = exercise.reps,
                        weight = exercise.weight,
                        category = exercise.category,
                        date = Calendar.getInstance()
                    )
                    ex.date.set(to.get(Calendar.YEAR), to.get(Calendar.MONTH), to.get(Calendar.DAY_OF_MONTH),from.get(Calendar.HOUR), from.get(Calendar.MINUTE), from.get(Calendar.SECOND))
                    database.FinishedExerciseDao().insert(ex)
                }
            }
        }
        Toast.makeText(requireContext().applicationContext,"Exercises copied successful", Toast.LENGTH_SHORT).show()
    }

    private fun startOfDay(day: Calendar): Calendar{
        val ret = Calendar.getInstance()
        ret.set(day.get(Calendar.YEAR), day.get(Calendar.MONTH), day.get(Calendar.DAY_OF_MONTH),0,0,0)
        return ret
    }

    private fun endOfDay(day: Calendar): Calendar{
        val ret = Calendar.getInstance()
        ret.set(day.get(Calendar.YEAR), day.get(Calendar.MONTH), day.get(Calendar.DAY_OF_MONTH),23,59,59)
        return ret
    }
}