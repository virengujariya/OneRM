package me.fitbod.repetition.mappers

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LineToWorkoutHistoryEntityMapperTest {
    private val mapper = LineToWorkoutHistoryEntityMapper()

    @Test
    fun `when valid line string, then return entity instance`() {
        // given
        val line = "Oct 11 2020,Back Squat,1,10,45"

        // when
        val entity = mapper.map(line)

        // then
        entity.shouldNotBeNull()
        entity.apply {
            exerciseDate.shouldBe(LocalDate.of(2020, 10, 11))
            exerciseName.shouldBe("Back Squat")
            sets.shouldBe(1)
            reps.shouldBe(10)
            weight.shouldBe(45)
        }
    }

    @Test
    fun `when no reps, then return null entity`() {
        // given
        val line = "Oct 11 2020,Back Squat,1,45"

        // when
        val entity = mapper.map(line)

        // then
        entity.shouldBeNull()
    }

    @Test
    fun `when invalid exercise date, then return null entity`() {
        // given
        val line = "Oct 34 2020,Back Squat,1,10,45"

        // when
        val entity = mapper.map(line)

        // then
        entity.shouldBeNull()
    }

    @Test
    fun `when sets 0, then return null entity`() {
        // given
        val line = "Oct 11 2020,Back Squat,0,10,45"

        // when
        val entity = mapper.map(line)

        // then
        entity.shouldBeNull()
    }
}