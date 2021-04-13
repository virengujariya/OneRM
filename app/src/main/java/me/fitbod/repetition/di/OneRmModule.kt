package me.fitbod.repetition.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.fitbod.repetition.BrzyckiOneRmCalculator
import me.fitbod.repetition.DefaultDispatcherProvider
import me.fitbod.repetition.DispatcherProvider
import me.fitbod.repetition.OneRmCalculator
import me.fitbod.repetition.data.WorkoutHistoryDao
import me.fitbod.repetition.db.OneRmDatabase
import me.fitbod.repetition.db.PrepopulateWorkoutHistory
import me.fitbod.repetition.db.PrepopulateWorkoutHistoryImpl
import me.fitbod.repetition.db.WorkoutHistoryCallback
import me.fitbod.repetition.repos.WorkoutHistoryRepo
import me.fitbod.repetition.repos.WorkoutHistoryRepoImpl
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OneRmModule {
    companion object {
        @Singleton
        @Provides
        fun provideDatabase(
            @ApplicationContext context: Context,
            callback: Provider<WorkoutHistoryCallback>
        ): OneRmDatabase {
            return Room.databaseBuilder(context, OneRmDatabase::class.java, "one-rm")
                .fallbackToDestructiveMigration()
                .addCallback(callback.get())
                .build()
        }

        @Provides
        fun provideWorkoutHistoryCallback(
            @ApplicationContext context: Context,
            prepopulateWorkoutHistory: PrepopulateWorkoutHistory,
            workoutHistoryDao: Provider<WorkoutHistoryDao>
        ): WorkoutHistoryCallback {
            return WorkoutHistoryCallback(
                context = context,
                prepopulateWorkoutHistory = prepopulateWorkoutHistory,
                workoutHistoryDao = workoutHistoryDao
            )
        }

        @Provides
        fun provideWorkoutHistoryDao(db: OneRmDatabase): WorkoutHistoryDao = db.workoutHistoryDao()
    }

    @Binds
    abstract fun bindOneRmCalculator(impl: BrzyckiOneRmCalculator): OneRmCalculator

    @Singleton
    @Binds
    abstract fun bindWorkoutHistoryRepo(impl: WorkoutHistoryRepoImpl): WorkoutHistoryRepo

    @Binds
    abstract fun bindPrepopulateWorkoutHistory(impl: PrepopulateWorkoutHistoryImpl): PrepopulateWorkoutHistory

    @Binds
    abstract fun bindDispatcher(impl: DefaultDispatcherProvider): DispatcherProvider
}