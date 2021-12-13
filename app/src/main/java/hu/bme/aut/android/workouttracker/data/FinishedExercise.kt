package hu.bme.aut.android.workouttracker.data

import androidx.room.*
import java.util.*

@Entity(tableName = "finishedExercise")
//@TypeConverters(Exercise::class)
data class FinishedExercise(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "category") var category: Category,
    @ColumnInfo(name = "weight") var weight: Int,
    @ColumnInfo(name = "reps") var reps: Int,
    @ColumnInfo(name = "date") var date: Calendar
){
    enum class Category {
        ABS, BACK, BICEP, CHEST, LEG, SHOULDER, TRICEPS, OTHER;
        companion object {
            @JvmStatic
            @TypeConverter
            fun getByOrdinal(ordinal: Int): Category? {
                var ret: Category? = null
                for (cat in values()) {
                    if (cat.ordinal == ordinal) {
                        ret = cat
                        break
                    }
                }
                return ret
            }

            @JvmStatic
            @TypeConverter
            fun toInt(category: Category): Int {
                return category.ordinal
            }
        }
    }


    class DateConverter {
        @TypeConverter
        fun fromTimestamp(value: Long): Calendar? {
            val c = Calendar.getInstance()
            c.setTimeInMillis(value)
            return c
        }

        @TypeConverter
        fun dateToTimestamp(date: Calendar): Long? {
            return date.timeInMillis
        }
    }
}

//annotation class exercise{
//    companion object{
//        @JvmStatic
//        @TypeConverter
//        fun name
//    }
//}
