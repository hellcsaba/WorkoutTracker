package hu.bme.aut.android.workouttracker.model

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.workouttracker.R
import hu.bme.aut.android.workouttracker.data.Exercise
import hu.bme.aut.android.workouttracker.data.ExerciseDao
import hu.bme.aut.android.workouttracker.data.ExercisesDatabase
import hu.bme.aut.android.workouttracker.data.FinishedExercise
import hu.bme.aut.android.workouttracker.databinding.NewExerciseDialogFragmentBinding
import java.util.*
import kotlin.concurrent.thread

class NewExerciseDialogFragment(private val listener: NewExerciseDialogListener): DialogFragment() {
    interface NewExerciseDialogListener {
        fun onExerciseCreated(newExercise: FinishedExercise)
    }

    private lateinit var binding: NewExerciseDialogFragmentBinding
    private lateinit var databaseAutoComplete: ExercisesDatabase

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        databaseAutoComplete = ExercisesDatabase.getDatabase(requireContext().applicationContext)

        binding = NewExerciseDialogFragmentBinding.inflate(LayoutInflater.from(context))
        binding.spCategory.adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.category_items)
        )

        setAdapterAutoTV()

        return AlertDialog.Builder(requireContext())
            .setTitle("Add finished exercise")
            .setView(binding.root)
            .setPositiveButton("OK") { dialogInterface, i ->
                if (isValid()) {
                    listener.onExerciseCreated(getFinishedExercise())
                }
            }

            .setNegativeButton("Cancel", null)
            .create()
    }
    private fun isValid() = binding.autotvName.text.isNotEmpty()

    private fun getFinishedExercise():FinishedExercise {
        return FinishedExercise(
            name = binding.autotvName.text.toString(),
            reps = binding.etReps.text.toString().toIntOrNull() ?: 0,
            weight = binding.etWeight.text.toString().toIntOrNull() ?: 0,
            category = FinishedExercise.Category.getByOrdinal(binding.spCategory.selectedItemPosition) ?: FinishedExercise.Category.OTHER,
            date = Calendar.getInstance()
        )
    }


    private fun setAdapterAutoTV(){
        thread {
            val exerciseNameList = ArrayList<String>()
            val exerciseList = databaseAutoComplete.ExercisesDao().getAll()
            for (exercise in exerciseList) {
                exerciseNameList.add(exercise.name)
            }
            requireActivity().runOnUiThread {
                binding.autotvName.setAdapter(
                    ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        exerciseNameList
                    )
                )
            }
        }
    }

    companion object {
        const val TAG = "NewExerciseDialogFragment"
    }
}