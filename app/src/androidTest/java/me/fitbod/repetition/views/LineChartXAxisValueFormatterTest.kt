package me.fitbod.repetition.views

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.kotest.matchers.shouldBe
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LineChartXAxisValueFormatterTest {
    private val formatter = LineChartXAxisValueFormatter()

    @Test
    fun axisLabelFormatIsCorrect() {
        // given
        val point = 18546f

        // when
        val result = formatter.getFormattedValue(point)

        // then
        result.shouldBe("Oct 11")
    }
}