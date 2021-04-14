package me.fitbod.repetition

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class BrzyckiOneRmCalculatorTest {
    private val calculator = BrzyckiOneRmCalculator()

    @Test
    fun `when supplied reps and weght, then returns Brzycki one RM`() = runBlockingTest {
        // given
        val reps = 10
        val weight = 100 // lbs

        // when
        val oneRm = calculator.calculate(reps, weight)

        // then
        "%.2f".format(oneRm).shouldBe("133.33")
    }
}