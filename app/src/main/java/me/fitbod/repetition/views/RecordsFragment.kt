package me.fitbod.repetition.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.fitbod.repetition.databinding.FragmentRecordsBinding

@AndroidEntryPoint
class RecordsFragment : Fragment() {
    private var _binding: FragmentRecordsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RecordsViewModel>()

    private val adapter = RecordsAdapter { exerciseName: String, oneRm: Int ->
        viewModel.onItemClicked(exerciseName, oneRm)
    }

    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setRecyclerViewAdapter()
        registerObservers()
        viewModel.loadWorkouts()
    }

    private fun initRecyclerView() {
        binding.recyclerViewWorkouts.setHasFixedSize(true)
        val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.recyclerViewWorkouts.addItemDecoration(divider)
    }

    private fun setRecyclerViewAdapter() {
        binding.recyclerViewWorkouts.adapter = adapter
    }

    private fun registerObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.workouts().collect { items -> adapter.submitList(items) }
        }
    }

    override fun onStart() {
        super.onStart()
        job = viewModel.command().onEach { command ->
            when (command) {
                is RecordsViewModel.Command.NavToWorkoutDetails -> navigateToWorkoutDetails(
                    command.exerciseName,
                    command.oneRm
                )

            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
    }

    private fun navigateToWorkoutDetails(exerciseName: String, oneRm: Int) {
        val action = RecordsFragmentDirections.actionWorkoutChartFragment(exerciseName, oneRm)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job = null
        binding.recyclerViewWorkouts.adapter = null
        _binding = null
    }
}