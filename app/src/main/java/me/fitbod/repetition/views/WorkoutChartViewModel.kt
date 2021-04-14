package me.fitbod.repetition.views

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.fitbod.repetition.DispatcherProvider
import me.fitbod.repetition.domain.ExerciseHistory
import me.fitbod.repetition.domain.WorkoutHistoryUseCase
import javax.inject.Inject

@HiltViewModel
class WorkoutChartViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val workoutHistoryUseCase: WorkoutHistoryUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val exerciseName = MutableStateFlow("")
    fun exerciseName(): StateFlow<String> = exerciseName

    private val oneRm = MutableStateFlow("")
    fun oneRm(): StateFlow<String> = oneRm

    private val entries = MutableStateFlow<List<Entry>>(emptyList())
    fun entries(): StateFlow<List<Entry>> = entries

    fun loadWorkoutDetails() {
        viewModelScope.launch(dispatcherProvider.default()) {
            exerciseName.value = savedStateHandle.get<String?>("exercise_name")
                ?: throw NullPointerException("exercise_name can not be null")
            oneRm.value = savedStateHandle.get<Int?>("one_rm_record")?.toString()
                ?: throw NullPointerException("one_rm_record can not be null")

            workoutHistoryUseCase.getExerciseHistory(exerciseName.value).collect { exercises ->
                entries.value = exercises.map { exercise -> exercise.mapToEntry() }
            }
        }
    }

    private fun ExerciseHistory.mapToEntry() = Entry(
        this.exerciseDate.toEpochDay().toFloat(),
        this.oneRm.toFloat()
    )
}