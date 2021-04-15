package me.fitbod.repetition.repos

import kotlinx.coroutines.flow.Flow
import me.fitbod.repetition.data.WorkoutHistoryDao
import me.fitbod.repetition.data.WorkoutHistoryEntity
import javax.inject.Inject

interface WorkoutHistoryRepo {
    suspend fun getWorkoutHistory(): Flow<List<WorkoutHistoryEntity>>
    suspend fun getWorkoutHistory(exerciseName: String): Flow<List<WorkoutHistoryEntity>>
}

class WorkoutHistoryRepoImpl @Inject constructor(
    private val workoutHistoryDao: WorkoutHistoryDao
) : WorkoutHistoryRepo {
    override suspend fun getWorkoutHistory(): Flow<List<WorkoutHistoryEntity>> {
        return workoutHistoryDao
            .getWorkoutHistoryDistinctUntilChanged()
    }

    override suspend fun getWorkoutHistory(exerciseName: String): Flow<List<WorkoutHistoryEntity>> {
        return workoutHistoryDao
            .getWorkoutHistoryDistinctUntilChanged(exerciseName)
    }
}