package me.fitbod.repetition.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface WorkoutHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entities: List<WorkoutHistoryEntity>): List<Long>

    @Query("SELECT id, exercise_date, exercise_name, sets, reps, weight FROM workout_history")
    fun getWorkoutHistory(): Flow<List<WorkoutHistoryEntity>>

    fun getWorkoutHistoryDistinctUntilChanged() = getWorkoutHistory().distinctUntilChanged()

    @Query("SELECT id, exercise_date, exercise_name, sets, reps, weight FROM workout_history WHERE exercise_name=:exerciseName")
    fun getWorkoutHistory(exerciseName: String): Flow<List<WorkoutHistoryEntity>>

    fun getWorkoutHistoryDistinctUntilChanged(exerciseName: String) =
        getWorkoutHistory(exerciseName).distinctUntilChanged()
}