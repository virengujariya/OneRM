package me.fitbod.repetition.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.fitbod.repetition.data.WorkoutHistoryDao
import me.fitbod.repetition.data.WorkoutHistoryEntity

@Database(entities = [WorkoutHistoryEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class OneRmDatabase : RoomDatabase() {
    abstract fun workoutHistoryDao(): WorkoutHistoryDao
}