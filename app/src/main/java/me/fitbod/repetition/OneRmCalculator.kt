package me.fitbod.repetition

import javax.inject.Inject

interface OneRmCalculator {
    suspend fun calculate(reps: Int, weight: Int): Float
}

/**
 * Calculate OneRM using Brzycki Formula
 * Ref: https://en.wikipedia.org/wiki/One-repetition_maximum
 */
class BrzyckiOneRmCalculator @Inject constructor() : OneRmCalculator {
    override suspend fun calculate(reps: Int, weight: Int): Float {
        return weight * (36 / (37 - reps).toFloat())
    }
}
