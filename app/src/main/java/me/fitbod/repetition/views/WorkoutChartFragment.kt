package me.fitbod.repetition.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import me.fitbod.repetition.databinding.FragmentWorkoutChartBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class WorkoutChartFragment : Fragment() {
    private var _binding: FragmentWorkoutChartBinding? = null
    private val binding get() = _binding!!

    private val chart
        get() = binding.lineChart

    private val viewModel by viewModels<WorkoutChartViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLineChart()
        lifecycleScope.launchWhenStarted {
            viewModel.entries().collect { items ->
                if (items.isEmpty()) return@collect
                val lineDataSet = LineDataSet(items, "DataSet")
                val lineData = LineData(lineDataSet)
                chart.data = lineData
                chart.notifyDataSetChanged()
                chart.invalidate()
                // chart.setVisibleXRangeMaximum(5f)
            }
        }
    }

    private fun initLineChart() {
        chart.setDrawGridBackground(false)
        val xAxis = chart.xAxis
        xAxis.valueFormatter = LineChartXAxisValueFormatter()
        xAxis.textSize = 11f
        // xAxis.setCenterAxisLabels(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        // xAxis.setDrawAxisLine(true)
        // xAxis.setDrawGridLines(true)
    }

    class LineChartXAxisValueFormatter : IndexAxisValueFormatter() {
        private val formatter = DateTimeFormatter.ofPattern("MMM d")
        override fun getFormattedValue(value: Float): String {
            return LocalDate.ofEpochDay(value.toLong()).format(formatter)
        }
    }
}