package me.fitbod.repetition.views

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import me.fitbod.repetition.BrzyckiOneRmCalculator
import me.fitbod.repetition.CoroutineTestRule
import me.fitbod.repetition.DispatcherProvider
import me.fitbod.repetition.data.WorkoutHistoryDao
import me.fitbod.repetition.data.WorkoutHistoryEntity
import me.fitbod.repetition.domain.WorkoutHistoryUseCase
import me.fitbod.repetition.repos.WorkoutHistoryRepoImpl
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class RecordsViewModelTest {
    @Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var dispatcherRule = CoroutineTestRule()

    private val dispatcherProvider = object : DispatcherProvider {
        override fun default(): CoroutineDispatcher = dispatcherRule.testDispatcher
        override fun io(): CoroutineDispatcher = dispatcherRule.testDispatcher
        override fun main(): CoroutineDispatcher = dispatcherRule.testDispatcher
    }

    lateinit var viewModel: RecordsViewModel

    private val dao = mockk<WorkoutHistoryDao>()

    private val repo = WorkoutHistoryRepoImpl(dao)
    private val calculator = BrzyckiOneRmCalculator()
    private val useCase = WorkoutHistoryUseCase(
        dispatcherProvider = dispatcherProvider,
        workoutHistoryRepo = repo,
        oneRmCalculator = calculator
    )

    @BeforeEach
    fun setup() {
        viewModel = RecordsViewModel(dispatcherProvider, useCase)
    }

    @Test
    fun `when workout history returns empty list, then workouts StateFlow value is empty`() =
        dispatcherRule.testDispatcher.runBlockingTest {
            // given
            coEvery { dao.getWorkoutHistoryDistinctUntilChanged() } returns flow {
                emit(emptyList<WorkoutHistoryEntity>())
            }

            val records = mutableListOf<RecordViewData>()
            val job = launch(dispatcherRule.testDispatcher) {
                viewModel.workouts().collect {
                    records.clear()
                    records.addAll(it)
                }
            }

            // when
            viewModel.loadWorkouts()

            // then
            records.shouldBeEmpty()
            job.cancel()
        }

    @Test
    fun `when workout history returns list, then workouts StateFlow value reflects`() =
        dispatcherRule.testDispatcher.runBlockingTest {
            // given
            val entity1 = WorkoutHistoryEntity(LocalDate.of(2020, 10, 11), "exercise1", 1, 10, 45)
            val entity2 = WorkoutHistoryEntity(LocalDate.of(2020, 10, 12), "exercise2", 1, 10, 135)
            coEvery { dao.getWorkoutHistoryDistinctUntilChanged() } returns flow {
                emit(listOf(entity1, entity2))
            }

            val records = mutableListOf<RecordViewData>()
            val job = launch(dispatcherRule.testDispatcher) {
                viewModel.workouts().collect {
                    records.clear()
                    records.addAll(it)
                }
            }

            // when
            viewModel.loadWorkouts()

            records.size.shouldBe(2)

            job.cancel()
        }

    @Test
    fun `when workout item is clicked, then command is send`() = dispatcherRule.testDispatcher.runBlockingTest {
        // given
        val job = launch(dispatcherRule.testDispatcher) {
            viewModel.command().collect { command ->
                // then
                if (command is RecordsViewModel.Command.NavToWorkoutDetails) {
                    command.exerciseName.shouldBe("exercise1")
                    command.oneRm.shouldBe(100)
                }
            }
        }

        // when
        viewModel.onItemClicked("exercise1", 100)

        // then
        job.cancel()
    }
}