package hu.bme.aut.android.workouttracker.model

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.workouttracker.R
import hu.bme.aut.android.workouttracker.data.FinishedExercise
import hu.bme.aut.android.workouttracker.databinding.NewExerciseDialogFragmentBinding
import java.util.*

class NewExerciseDialogFragment(private val listener: NewExerciseDialogListener): DialogFragment() {
    interface NewExerciseDialogListener {
        fun onExerciseCreated(newExercise: FinishedExercise)
    }

    private lateinit var binding: NewExerciseDialogFragmentBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = NewExerciseDialogFragmentBinding.inflate(LayoutInflater.from(context))
        binding.spCategory.adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.category_items)
        )

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
    private fun isValid() = binding.etName.text.isNotEmpty()

    private fun getFinishedExercise() = FinishedExercise(
        name = binding.etName.text.toString(),
        reps = binding.etReps.text.toString().toIntOrNull() ?: 0,
        weight = binding.etWeight.text.toString().toIntOrNull() ?: 0,
        category = FinishedExercise.Category.getByOrdinal(binding.spCategory.selectedItemPosition) ?: FinishedExercise.Category.OTHER,
        date = Calendar.getInstance()
    )


    companion object {
        const val TAG = "NewExerciseDialogFragment"
    }
}