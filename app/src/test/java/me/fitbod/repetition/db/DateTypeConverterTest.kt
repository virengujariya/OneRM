package me.fitbod.repetition.db

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDate

class DateTypeConverterTest {
    private val dateTypeConverter = DateTypeConverter()

    @Test
    fun `epochDay Long to LocalDate should convert successfully`() {
        // given
        val epochDay = 18546L

        // when
        val localDate = dateTypeConverter.from(epochDay)

        // then
        localDate.shouldBe(LocalDate.of(2020, 10, 11))
    }

    @Test
    fun `LocalDate to Long epochDay should convert successfully`() {
        // given
        val localDate = LocalDate.of(2020, 10, 11)

        // when
        val epochDay = dateTypeConverter.to(localDate)

        // then
        epochDay.shouldBe(18546L)
    }
}