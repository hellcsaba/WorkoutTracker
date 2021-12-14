package hu.bme.aut.android.workouttracker.model

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.workouttracker.R
import hu.bme.aut.android.workouttracker.adapter.ExerciseAdapter
import hu.bme.aut.android.workouttracker.data.Exercise
import hu.bme.aut.android.workouttracker.data.ExercisesDatabase
import hu.bme.aut.android.workouttracker.databinding.CreateExerciseFragmentBinding
import kotlin.concurrent.thread


class CreateExerciseFragment : Fragment(), ExerciseAdapter.ExerciseClickListener {

    private lateinit var database: ExercisesDatabase
    private lateinit var binding: CreateExerciseFragmentBinding
    private lateinit var adapter: ExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = ExercisesDatabase.getDatabase(requireContext().applicationContext)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = CreateExerciseFragmentBinding.inflate(LayoutInflater.from(context))

        binding.spCategory.adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.category_items)
        )

        binding.btAdd.setOnClickListener { onExerciseCreated() }
        initRecyclerView()
        loadItemsInBackground()
        return binding.root

    }

    private fun initRecyclerView() {
        binding.rvExercises.layoutManager = LinearLayoutManager(requireActivity())
        adapter = ExerciseAdapter(this)
        binding.rvExercises.adapter = adapter
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.ExercisesDao().getAll()
            requireActivity().runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onExerciseCreated() {
        if(!binding.etName.text.isEmpty()) {
            val newItem = Exercise(
                name = binding.etName.text.toString(),
                category = Exercise.Category.getByOrdinal(binding.spCategory.selectedItemPosition)
                    ?: Exercise.Category.OTHER
            )

            thread {
                val ex = database.ExercisesDao().getExerciseByName(binding.etName.text.toString())
                if (ex == null) {
                    newItem.id = database.ExercisesDao().insert(newItem)
                    Log.d("CreateExerciseFragment", "ExerciseCreated")
                    requireActivity().runOnUiThread {
                        adapter.addItem(newItem)
                        Toast.makeText(
                            requireContext().applicationContext,
                            "Exercise added successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    requireActivity().runOnUiThread {
                        Toast.makeText(
                            requireContext().applicationContext,
                            "Exercise already exists",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else {
            requireActivity().runOnUiThread {
                Toast.makeText(
                    requireContext().applicationContext,
                    "Name is required",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onExerciseDeleted(item: Exercise) {
        thread{
            database.ExercisesDao().deleteItem(item)
            Log.d("CreateExerciseFragment"," Exercise delete was successful")

            requireActivity().runOnUiThread {
                adapter.deleteItem(item)
            }
        }
    }



}