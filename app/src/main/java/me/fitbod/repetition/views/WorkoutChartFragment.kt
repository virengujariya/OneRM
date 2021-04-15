package me.fitbod.repetition.views

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import me.fitbod.repetition.R
import me.fitbod.repetition.databinding.FragmentWorkoutChartBinding
import me.fitbod.repetition.getColorFromAttr

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
        setToolBarNavigation()
        configureChart()
        configureAxis()
        registerObservers()
        viewModel.loadWorkoutDetails()
    }

    private fun setToolBarNavigation() {
        binding.toolbar.navigationIcon?.setTint(requireContext().getColorFromAttr(R.attr.colorOnPrimary))
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun registerObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.entries().collect { entries ->
                if (entries.isEmpty()) return@collect
                setChartData(entries)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.exerciseName().collect { name ->
                binding.toolbar.title = name
                binding.item.textViewExercise.text = name
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.oneRm().collect { oneRm -> binding.item.textViewWeight.text = oneRm }
        }
    }

    private fun configureChart() {
        chart.apply {
            setBackgroundColor(Color.TRANSPARENT)
            setDrawGridBackground(false)
            setTouchEnabled(false)
            setPinchZoom(false)
            description.isEnabled = false
            legend.isEnabled = false
            axisRight.isEnabled = false
            extraBottomOffset = 5f
            extraLeftOffset = 5f
        }
    }

    private fun configureAxis() {
        chart.xAxis.apply {
            setAvoidFirstLastClipping(true)
            setDrawAxisLine(false)
            textSize = 14f
            textColor = requireContext().getColorFromAttr(R.attr.chartAxisTextColor)
            valueFormatter = LineChartXAxisValueFormatter()
            position = XAxis.XAxisPosition.BOTTOM
        }

        chart.axisLeft.apply {
            textSize = 14f
            setDrawAxisLine(false)
            setDrawZeroLine(true)
            textColor = requireContext().getColorFromAttr(R.attr.chartAxisTextColor)
            valueFormatter = LineChartYAxisValueFormatter(requireContext())
        }
    }

    private fun setChartData(entries: List<Entry>) {
        val dataSet = chart.data?.getDataSetByLabel(LINE_DATA_SET_LABEL, true) as LineDataSet?
        if (dataSet != null) {
            // update data-set
            dataSet.values = entries
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create new data-set
            val newDataSet = LineDataSet(entries, LINE_DATA_SET_LABEL)
            newDataSet.apply {
                color = requireContext().getColorFromAttr(R.attr.colorPrimaryVariant)
                setCircleColor(requireContext().getColorFromAttr(R.attr.colorPrimaryVariant))
                lineWidth = 2f
                circleRadius = 4f
                setDrawCircleHole(false)
                setDrawValues(false)
            }
            val newLineData = LineData(newDataSet)
            chart.data = newLineData
            chart.notifyDataSetChanged()
            chart.invalidate()
        }
    }

    companion object {
        private const val LINE_DATA_SET_LABEL = "LineDataSet"
    }
}