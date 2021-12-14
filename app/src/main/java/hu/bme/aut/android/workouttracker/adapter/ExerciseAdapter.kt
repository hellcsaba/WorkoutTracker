package hu.bme.aut.android.workouttracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.workouttracker.R
import hu.bme.aut.android.workouttracker.data.Exercise
import hu.bme.aut.android.workouttracker.data.FinishedExercise
import hu.bme.aut.android.workouttracker.databinding.ItemCreateExerciseBinding


class ExerciseAdapter(private val listener: ExerciseClickListener): RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {
    private val exercises = mutableListOf<Exercise>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ExerciseViewHolder(
        ItemCreateExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ExerciseAdapter.ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.binding.tvName.text = exercise.name
        holder.binding.tvCategory.text = exercise.category.toString()
        holder.binding.ivIcon.setImageResource(getImageResource(exercise.category))

        holder.binding.ibRemove.setOnClickListener {
            listener.onExerciseDeleted(exercise)
        }
    }

    @DrawableRes()
    private fun getImageResource(category: Exercise.Category): Int {
        return when (category) {
            Exercise.Category.ABS -> R.drawable.icon_abs_50
            Exercise.Category.CHEST -> R.drawable.icon_chest_50
            Exercise.Category.LEG -> R.drawable.icon_leg_50
            Exercise.Category.BICEP -> R.drawable.icon_bicep_50
            Exercise.Category.TRICEPS -> R.drawable.icon_triceps_50
            Exercise.Category.BACK -> R.drawable.icon_back_50
            Exercise.Category.SHOULDER -> R.drawable.icon_shoulders_50
            Exercise.Category.OTHER -> R.drawable.icon_body_50
        }
    }

    fun addItem(item: Exercise) {
        exercises.add(item)
        notifyItemInserted(exercises.size - 1)
    }

    fun update(shoppingItems: List<Exercise>) {
        exercises.clear()
        exercises.addAll(shoppingItems)
        notifyDataSetChanged()
    }

    fun deleteItem(item: Exercise){
        exercises.remove(item);
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = exercises.size

    interface ExerciseClickListener{
        fun onExerciseCreated()
        fun onExerciseDeleted(exercise: Exercise)
    }

    inner class ExerciseViewHolder(val binding: ItemCreateExerciseBinding) : RecyclerView.ViewHolder(binding.root)

}