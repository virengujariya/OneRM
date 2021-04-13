package me.fitbod.repetition.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "workout_history")
data class WorkoutHistoryEntity(
    @ColumnInfo(name = "exercise_date")
    val exerciseDate: LocalDate,

    @ColumnInfo(name = "exercise_name", index = true)
    val exerciseName: String,

    @ColumnInfo(name = "sets")
    val sets: Int,

    @ColumnInfo(name = "reps")
    val reps: Int,

    @ColumnInfo(name = "weight")
    val weight: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}