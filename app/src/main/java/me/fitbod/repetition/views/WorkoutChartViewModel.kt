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
import me.fitbod.repetition.domain.WorkoutHistoryUseCase
import javax.inject.Inject

@HiltViewModel
class WorkoutChartViewModel @Inject constructor(
    private val workoutHistoryUseCase: WorkoutHistoryUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val exerciseName = MutableStateFlow(savedStateHandle.get<String?>("exercise_name"))
    fun exerciseName(): StateFlow<String?> = exerciseName

    private val oneRm = MutableStateFlow(savedStateHandle.get<Int?>("one_rm_record"))
    fun oneRm(): StateFlow<Int?> = oneRm

    private val entries = MutableStateFlow<List<Entry>>(emptyList())
    fun entries(): StateFlow<List<Entry>> = entries

    init {
        savedStateHandle.get<String?>("exercise_name")?.let { name ->
            loadWorkoutDetails(name)
        }
    }

    private fun loadWorkoutDetails(exerciseName: String) {
        viewModelScope.launch {
            workoutHistoryUseCase.getWorkoutHistory(exerciseName).collect { exercises ->
                entries.value = exercises.map { exercise ->
                    Entry(
                        exercise.exerciseDate.toEpochDay().toFloat(),
                        exercise.oneRm.toFloat()
                    )
                }
            }
        }
    }
}