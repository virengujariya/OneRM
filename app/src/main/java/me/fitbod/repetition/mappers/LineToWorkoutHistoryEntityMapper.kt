package me.fitbod.repetition.mappers

import me.fitbod.repetition.data.WorkoutHistoryEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import javax.inject.Inject

class LineToWorkoutHistoryEntityMapper @Inject constructor() : Mapper<String, WorkoutHistoryEntity?> {
    private val formatter = DateTimeFormatter.ofPattern("MMM dd yyyy", Locale.ENGLISH)
    override fun map(input: String): WorkoutHistoryEntity? {
        val split = input.split(",".toRegex())
        if (split.size == 5) {
            val date = try {
                LocalDate.parse(split[0], formatter)
            } catch (e: DateTimeParseException) {
                return null
            }
            val exercise = split[1]
            val sets = split[2].toInt()
            val reps = split[3].toInt()
            val weight = split[4].toInt()
            if (date != null && exercise.isNotEmpty() && sets > 0 && reps > 0 && weight > 0) {
                return WorkoutHistoryEntity(
                    exerciseDate = date,
                    exerciseName = exercise,
                    sets = sets,
                    reps = reps,
                    weight = weight
                )
            }
        }
        return null
    }
}