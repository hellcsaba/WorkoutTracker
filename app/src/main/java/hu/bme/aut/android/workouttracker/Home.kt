package hu.bme.aut.android.workouttracker

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.workouttracker.adapter.FinishedExerciseAdapter
import hu.bme.aut.android.workouttracker.data.FinishedExercise
import hu.bme.aut.android.workouttracker.data.FinishedExerciseDao
import hu.bme.aut.android.workouttracker.data.FinishedExercisesDatabase
import hu.bme.aut.android.workouttracker.databinding.FragmentHomeBinding
import hu.bme.aut.android.workouttracker.model.NewExerciseDialogFragment
import hu.bme.aut.android.workouttracker.model.SearchDayDialogFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class Home : Fragment(), FinishedExerciseAdapter.DeleteExerciseClickListener,
    SearchDayDialogFragment.SearchDayDialogListener,
        NewExerciseDialogFragment.NewExerciseDialogListener,
        Copy.CopyFragmentListener
{
    private lateinit var database: FinishedExercisesDatabase
    private lateinit var adapter: FinishedExerciseAdapter
    private lateinit var binding: FragmentHomeBinding
    private var initDate = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FinishedExercisesDatabase.getDatabase(requireContext().applicationContext)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(context))

        Log.d("HomeKt","onCreateView")
        binding.fab.setOnClickListener{
            NewExerciseDialogFragment(this).show(
                requireActivity().supportFragmentManager,
                NewExerciseDialogFragment.TAG
            )
        }

        binding.btDate.setOnClickListener{
            SearchDayDialogFragment(this).show(
                requireActivity().supportFragmentManager,
                SearchDayDialogFragment.TAG
            )
        }
        initRecyclerView()
        loadItemsInBackground(initDate)
        return binding.root
    }

    private fun initRecyclerView() {
        binding.rvHome.layoutManager = LinearLayoutManager(requireActivity())
        adapter = FinishedExerciseAdapter(this)
        binding.rvHome.adapter = adapter
    }

    fun loadItemsInBackground(day: Calendar) {
        thread {
            Log.d("HomeloadItemsBackground",day.toFormattedDate() )
            val items = database.FinishedExerciseDao().getExercisesByDate(startOfDay(day), endOfDay(day))
            requireActivity().runOnUiThread {
                if(!items.isEmpty())
                    binding.btDate.text =  items[0].date.toFormattedDate()
                else
                    binding.btDate.text = day.toFormattedDate()
                adapter.update(items)
            }
        }
    }

    override fun onExerciseDeleted(exercise: FinishedExercise){
        thread{
            database.FinishedExerciseDao().deleteItem(exercise)
            Log.d("HomeKt"," delete was successful")

            requireActivity().runOnUiThread {
                adapter.deleteExercise(exercise)
                Log.d(requireActivity().toString(),"onExerciseDeletedHomeKt")
            }
        }
    }


    private fun Calendar.toFormattedDate(): String{
        return when{
            DateUtils.isToday(this.timeInMillis + DateUtils.DAY_IN_MILLIS) -> "YESTERDAY"
            DateUtils.isToday(this.timeInMillis - DateUtils.DAY_IN_MILLIS) -> "TOMORROW"
            DateUtils.isToday(this.timeInMillis) -> "TODAY"
            else -> SimpleDateFormat("MM/dd/yyyy").format(this.time)
        }

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

    override fun onSearchDay(day: Calendar) {
        initDate = day
        loadItemsInBackground(day)
    }

    override fun onExerciseCreated(newExercise: FinishedExercise) {
        thread {
            newExercise.date = initDate
            database.FinishedExerciseDao().insert(newExercise)
            Log.d("Home", "onExerciseCreated")
            requireActivity().runOnUiThread {
                adapter.addExercise(newExercise)
            }
        }
    }

    override fun onCopyExercises(from: Calendar, to: Calendar) {
        thread{
            val exercises = database.FinishedExerciseDao().getExercisesByDate(startOfDay(from), endOfDay(from))
            if(!exercises.isEmpty()){
                for(exercise in exercises){
                    exercise.date.set(to.get(Calendar.YEAR), to.get(Calendar.MONTH), to.get(Calendar.DAY_OF_MONTH))
                    database.FinishedExerciseDao().insert(exercise)
                }
            }
            requireActivity().runOnUiThread {
                adapter.update(exercises)
            }
        }
    }


}