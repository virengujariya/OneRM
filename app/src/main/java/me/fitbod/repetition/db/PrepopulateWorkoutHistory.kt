package me.fitbod.repetition.db

import me.fitbod.repetition.data.WorkoutHistoryEntity
import me.fitbod.repetition.mappers.LineToWorkoutHistoryEntityMapper
import java.io.InputStream
import javax.inject.Inject

interface PrepopulateWorkoutHistory {
    fun createFromStream(inputStream: InputStream): List<WorkoutHistoryEntity>
}

class PrepopulateWorkoutHistoryImpl @Inject constructor(
    private val mapper: LineToWorkoutHistoryEntityMapper
) : PrepopulateWorkoutHistory {

    override fun createFromStream(inputStream: InputStream): List<WorkoutHistoryEntity> {
        val workoutHistories = mutableListOf<WorkoutHistoryEntity>()
        inputStream.bufferedReader().forEachLine { line ->
            val entity = mapper.map(line)
            if (entity != null) {
                workoutHistories.add(entity)
            }
        }
        return workoutHistories
    }
}
