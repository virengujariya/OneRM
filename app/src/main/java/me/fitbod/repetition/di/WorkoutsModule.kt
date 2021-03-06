package me.fitbod.repetition.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import me.fitbod.repetition.DispatcherProvider
import me.fitbod.repetition.OneRmCalculator
import me.fitbod.repetition.domain.WorkoutHistoryUseCase
import me.fitbod.repetition.repos.WorkoutHistoryRepo

@Module
@InstallIn(ViewModelComponent::class)
abstract class WorkoutsModule {
    companion object {
        @Provides
        @ViewModelScoped
        fun provideWorkoutUseCase(
            dispatcherProvider: DispatcherProvider,
            workoutHistoryRepo: WorkoutHistoryRepo,
            oneRmCalculator: OneRmCalculator
        ) = WorkoutHistoryUseCase(
            dispatcherProvider = dispatcherProvider,
            workoutHistoryRepo = workoutHistoryRepo,
            oneRmCalculator = oneRmCalculator
        )
    }
}