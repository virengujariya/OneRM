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
import me.fitbod.repetition.domain.WorkoutHistoryUseCase
import javax.inject.Inject

@HiltViewModel
class RecordsViewModel @Inject constructor(
    private val workoutHistoryUseCase: WorkoutHistoryUseCase
) : ViewModel() {
    private val workouts = MutableStateFlow<List<RecordViewData>>(emptyList())
    fun workouts(): StateFlow<List<RecordViewData>> = workouts

    private val command = Channel<Command>(Channel.BUFFERED)
    fun command(): Flow<Command> = command.receiveAsFlow()

    init {
        loadWorkouts()
    }

    private fun loadWorkouts() = viewModelScope.launch {
        workoutHistoryUseCase.getWorkoutWithOverallOneRm().collect { items ->
            workouts.value = items.map { item ->
                RecordViewData(exerciseName = item.exerciseName, oneRm = item.oneRm)
            }
        }
    }

    fun onItemClicked(exerciseName: String, oneRm: Int) {
        viewModelScope.launch {
            command.send(Command.NavToWorkoutDetails(exerciseName, oneRm))
        }
    }

    sealed class Command {
        data class NavToWorkoutDetails(val exerciseName: String, val oneRm: Int) : Command()
    }
}