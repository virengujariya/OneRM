package me.fitbod.repetition

import javax.inject.Inject

interface OneRmCalculator {
    suspend fun calculate(reps: Int, weight: Int): Float
}

class BrzyckiOneRmCalculator @Inject constructor() : OneRmCalculator {
    override suspend fun calculate(reps: Int, weight: Int): Float {
        return weight * (36 / (37 - reps).toFloat())
    }
}
