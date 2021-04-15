package me.fitbod.repetition.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.fitbod.repetition.DispatcherProvider
import me.fitbod.repetition.OneRmCalculator
import me.fitbod.repetition.repos.WorkoutHistoryRepo
import kotlin.math.roundToInt

class WorkoutHistoryUseCase(
    private val dispatcherProvider: DispatcherProvider,
    private val workoutHistoryRepo: WorkoutHistoryRepo,
    private val oneRmCalculator: OneRmCalculator
) {
    suspend fun getWorkoutOneRmRecords(): Flow<List<WorkoutRecord>> = withContext(dispatcherProvider.io()) {
        return@withContext workoutHistoryRepo.getWorkoutHistory().map { entities ->
            entities.groupBy { workout -> workout.exerciseName }
                .mapValues { entry ->
                    entry.value.map { workout ->
                        oneRmCalculator.calculate(workout.reps, workout.weight)
                    }.maxOrNull()?.roundToInt() ?: 0 // Max oneRM value among list items
                }.map { entry ->
                    WorkoutRecord(exerciseName = entry.key, maxOneRm = entry.value)
                }
        }
    }

    suspend fun getExerciseHistory(exerciseName: String): Flow<List<ExerciseHistory>> =
        withContext(dispatcherProvider.io()) {
            return@withContext workoutHistoryRepo.getWorkoutHistory(exerciseName).map { entities ->
                entities.groupBy { entity -> entity.exerciseDate }
                    .mapValues { entry ->
                        entry.value.map { entity ->
                            oneRmCalculator.calculate(entity.reps, entity.weight)
                        }.maxOrNull()?.roundToInt() ?: 0 // Max oneRM value among list items
                    }.map { entry ->
                        ExerciseHistory(exerciseDate = entry.key, oneRm = entry.value)
                    }.sortedBy { it.exerciseDate }
            }
        }
}