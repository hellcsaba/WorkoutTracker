package hu.bme.aut.android.workouttracker.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.workouttracker.R
import hu.bme.aut.android.workouttracker.data.FinishedExercise
import hu.bme.aut.android.workouttracker.databinding.ItemExerciseListBinding

class FinishedExerciseAdapter(private val listener: DeleteExerciseClickListener): RecyclerView.Adapter<FinishedExerciseAdapter.FinishedExerciseViewHolder>() {
    private val exercises = mutableListOf<FinishedExercise>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FinishedExerciseViewHolder(
        ItemExerciseListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: FinishedExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.binding.tvName.text = exercise.name.toString()
        holder.binding.tvReps.text = "Reps: ${exercise.reps.toString()}"
        holder.binding.tvWeight.text = "Weight: ${exercise.weight.toString()}"
        holder.binding.tvCategory.text = exercise.category.toString()
        holder.binding.ivIcon.setImageResource(getImageResource(exercise.category))
        holder.binding.ibRemove.setOnClickListener {
            listener.onExerciseDeleted(exercise)
        }
    }

    @DrawableRes()
    private fun getImageResource(category: FinishedExercise.Category): Int {
        return when (category) {
            FinishedExercise.Category.ABS -> R.drawable.icon_abs_50
            FinishedExercise.Category.CHEST -> R.drawable.icon_chest_50
            FinishedExercise.Category.LEG -> R.drawable.icon_leg_50
            FinishedExercise.Category.BICEP -> R.drawable.icon_bicep_50
            FinishedExercise.Category.TRICEPS -> R.drawable.icon_triceps_50
            FinishedExercise.Category.BACK -> R.drawable.icon_back_50
            FinishedExercise.Category.SHOULDER -> R.drawable.icon_shoulders_50
            FinishedExercise.Category.OTHER -> R.drawable.icon_body_50
        }
    }

    fun addExercise(exercise: FinishedExercise) {
        exercises.add(exercise)
        Log.d("FinishedExerciseAdapter","addExercise")
        notifyItemInserted(exercises.size - 1)
    }

    fun update(exs: List<FinishedExercise>) {
        exercises.clear()
        exercises.addAll(exs)
        notifyDataSetChanged()
    }

    fun deleteExercise(exercise: FinishedExercise){
        exercises.remove(exercise);
        Log.d("FinishedExerciseAdapter","deleteExercise")
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = exercises.size

    interface DeleteExerciseClickListener{
        fun onExerciseDeleted(exercise: FinishedExercise)

    }

    inner class FinishedExerciseViewHolder(val binding: ItemExerciseListBinding) : RecyclerView.ViewHolder(binding.root)
}