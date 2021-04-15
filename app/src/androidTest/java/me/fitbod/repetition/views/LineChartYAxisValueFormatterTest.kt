package me.fitbod.repetition.views

import androidx.test.core.app.ApplicationProvider
import io.kotest.matchers.shouldBe
import org.junit.Test

class LineChartYAxisValueFormatterTest {
    private val formatter = LineChartYAxisValueFormatter(ApplicationProvider.getApplicationContext())

    @Test
    fun axisLabelFormatIsCorrect() {
        // given
        val point = 180f

        // when
        val result = formatter.getFormattedValue(point)

        // then
        result.shouldBe("180 lbs")
    }
}