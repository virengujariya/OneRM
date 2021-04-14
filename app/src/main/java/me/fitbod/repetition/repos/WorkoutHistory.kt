package me.fitbod.repetition.repos

import java.time.LocalDate

data class WorkoutHistory(val exerciseDate: LocalDate, val exerciseName: String, val reps: Int, val weight: Int)