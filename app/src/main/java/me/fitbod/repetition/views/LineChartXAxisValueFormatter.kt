package me.fitbod.repetition.views

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class LineChartXAxisValueFormatter : IndexAxisValueFormatter() {
    private val formatter = DateTimeFormatter.ofPattern("MMM d", Locale.ENGLISH)
    override fun getFormattedValue(value: Float): String {
        return LocalDate.ofEpochDay(value.toLong()).format(formatter)
    }
}