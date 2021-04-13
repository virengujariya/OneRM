package me.fitbod.repetition.db

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.fitbod.repetition.data.WorkoutHistoryDao
import javax.inject.Provider

class WorkoutHistoryCallback constructor(
    @ApplicationContext private val context: Context,
    private val prepopulateWorkoutHistory: PrepopulateWorkoutHistory,
    private val workoutHistoryDao: Provider<WorkoutHistoryDao>,
    private val externalScope: CoroutineScope = GlobalScope,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        val inputStream = context.resources.assets.open("workout-data.txt")
        externalScope.launch(dispatcher) {
            val workoutEntities = prepopulateWorkoutHistory.createFromStream(inputStream)
            workoutHistoryDao.get().insert(workoutEntities)
        }
    }
}