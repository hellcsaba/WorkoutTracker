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
import hu.bme.aut.android.workouttracker.data.FinishedExercisesDatabase
import hu.bme.aut.android.workouttracker.databinding.FragmentHomeBinding
import hu.bme.aut.android.workouttracker.model.NewExerciseDialogFragment
import hu.bme.aut.android.workouttracker.model.SearchDayDialogFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class Home : Fragment(), FinishedExerciseAdapter.DeleteExerciseClickListener
    //SearchDayDialogFragment.SearchDayDialogListener
{
    private lateinit var database: FinishedExercisesDatabase
    private lateinit var adapter: FinishedExerciseAdapter
    private lateinit var binding: FragmentHomeBinding
    //private var initDate = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FinishedExercisesDatabase.getDatabase(requireContext().applicationContext)
       // listener =  context as? SearchHomeListener
            ?: throw RuntimeException("Activity must implement the SearchHomeListener interface!")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(context))

        Log.d("HomeKt","onCreateView")
        binding.fab.setOnClickListener{
            NewExerciseDialogFragment().show(
                requireActivity().supportFragmentManager,
                NewExerciseDialogFragment.TAG
            )
        }

        binding.btDate.setOnClickListener{
            SearchDayDialogFragment().show(
                requireActivity().supportFragmentManager,
                SearchDayDialogFragment.TAG
            )
        }
        initRecyclerView()
        loadItemsInBackground()
        return binding.root
    }

    private fun initRecyclerView() {
        binding.rvHome.layoutManager = LinearLayoutManager(requireActivity())
        adapter = FinishedExerciseAdapter(this)
        binding.rvHome.adapter = adapter
    }

    fun loadItemsInBackground() {
        thread {
            val items = database.FinishedExerciseDao().getExercisesByDate(Calendar.getInstance().startOfDay(), Calendar.getInstance().endOfDay())
            requireActivity().runOnUiThread {
                if(!items.isEmpty())
                    binding.btDate.text =  items[0].date.toFormattedDate()
                else
                    binding.btDate.text = Calendar.getInstance().toFormattedDate()
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