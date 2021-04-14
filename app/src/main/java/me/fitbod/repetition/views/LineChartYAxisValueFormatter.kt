package me.fitbod.repetition.views

import android.content.Context
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import me.fitbod.repetition.R

class LineChartYAxisValueFormatter(private val context: Context) : IndexAxisValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return context.getString(R.string.x_axis_weight_label, value.toInt())
    }
}