package me.fitbod.repetition.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.fitbod.repetition.OneRmCalculator
import me.fitbod.repetition.repos.WorkoutHistoryRepo
import kotlin.math.roundToInt

class WorkoutHistoryUseCase(
    private val workoutHistoryRepo: WorkoutHistoryRepo,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val oneRmCalculator: OneRmCalculator
) {
    suspend fun getWorkoutWithOverallOneRm(): Flow<List<WorkoutRecord>> = withContext(dispatcher) {
        return@withContext workoutHistoryRepo.getWorkoutHistory().map { entities ->
            return@map entities
                .groupBy { entity -> entity.exerciseName }
                .mapValues { entry ->
                    entry.value.map { entity ->
                        oneRmCalculator.calculate(entity.reps, entity.weight)
                    }.average().roundToInt()
                }.map { entry ->
                    WorkoutRecord(exerciseName = entry.key, oneRm = entry.value)
                }
        }
    }

    suspend fun getWorkoutHistory(exerciseName: String): Flow<List<ExerciseHistory>> = withContext(dispatcher) {
        return@withContext workoutHistoryRepo.getWorkoutHistory(exerciseName).map { entities ->
            return@map entities
                .groupBy { entity -> entity.exerciseDate }
                .mapValues { entry ->
                    entry.value.map { entity ->
                        oneRmCalculator.calculate(entity.reps, entity.weight)
                    }.average().roundToInt()
                }.map { entry ->
                    ExerciseHistory(exerciseDate = entry.key, oneRm = entry.value)
                }.sortedBy { it.exerciseDate }
        }
    }
}