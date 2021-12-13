package hu.bme.aut.android.workouttracker.model

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.workouttracker.databinding.SearchDayDialogFragmentBinding
import java.util.*

class SearchDayDialogFragment: DialogFragment() {
    interface SearchDayDialogListener{
        fun onSearchDay(day: Calendar)
    }

    private lateinit var listener: SearchDayDialogListener
    private lateinit var binding: SearchDayDialogFragmentBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? SearchDayDialogListener
            ?: throw RuntimeException("Activity must implement the NewShoppingItemDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = SearchDayDialogFragmentBinding.inflate(LayoutInflater.from(context))

        return AlertDialog.Builder(requireContext())
            .setTitle("Search day in diary")
            .setView(binding.root)
            .setPositiveButton("Search") { dialogInterface, i ->
                    listener.onSearchDay(binding.datePicker.getDate())
            }
            .setNegativeButton("Cancel", null)
            .create()
    }


    fun DatePicker.getDate(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        return calendar
    }

    companion object {
        const val TAG = "SearchDayDialogFragment"
    }
}