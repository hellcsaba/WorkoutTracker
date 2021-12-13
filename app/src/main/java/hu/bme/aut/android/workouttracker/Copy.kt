package hu.bme.aut.android.workouttracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hu.bme.aut.android.workouttracker.adapter.FinishedExerciseAdapter
import hu.bme.aut.android.workouttracker.data.FinishedExercisesDatabase
import hu.bme.aut.android.workouttracker.databinding.FragmentCopyBinding
import hu.bme.aut.android.workouttracker.databinding.FragmentHomeBinding

class Copy : Fragment()
{
    private lateinit var database: FinishedExercisesDatabase
    private lateinit var adapter: FinishedExerciseAdapter
    private lateinit var binding: FragmentCopyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FinishedExercisesDatabase.getDatabase(requireContext().applicationContext)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCopyBinding.inflate(LayoutInflater.from(context))
        binding.btnCopyExercises.setOnClickListener {  }
        return binding.root
    }


}