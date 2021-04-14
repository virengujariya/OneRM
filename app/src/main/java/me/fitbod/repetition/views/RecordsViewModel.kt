package me.fitbod.repetition.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.fitbod.repetition.DispatcherProvider
import me.fitbod.repetition.domain.WorkoutHistoryUseCase
import me.fitbod.repetition.domain.WorkoutRecord
import javax.inject.Inject

@HiltViewModel
class RecordsViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val workoutHistoryUseCase: WorkoutHistoryUseCase
) : ViewModel() {
    private val workouts = MutableStateFlow<List<RecordViewData>>(emptyList())
    fun workouts(): StateFlow<List<RecordViewData>> = workouts

    private val command = Channel<Command>(Channel.BUFFERED)
    fun command(): Flow<Command> = command.receiveAsFlow()

    fun loadWorkouts() {
        viewModelScope.launch(dispatcherProvider.default()) {
            workoutHistoryUseCase.getWorkoutOneRmRecords().collect { records ->
                workouts.value = records.map { record -> record.mapToViewData() }
            }
        }
    }

    fun onItemClicked(exerciseName: String, oneRm: Int) {
        viewModelScope.launch(dispatcherProvider.default()) {
            command.send(Command.NavToWorkoutDetails(exerciseName, oneRm))
        }
    }

    sealed class Command {
        data class NavToWorkoutDetails(val exerciseName: String, val oneRm: Int) : Command()
    }

    private fun WorkoutRecord.mapToViewData() = RecordViewData(
        exerciseName = this.exerciseName, oneRm = this.maxOneRm
    )
}
