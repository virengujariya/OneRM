package me.fitbod.repetition.domain

import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import me.fitbod.repetition.BrzyckiOneRmCalculator
import me.fitbod.repetition.CoroutineTestRule
import me.fitbod.repetition.DispatcherProvider
import me.fitbod.repetition.data.WorkoutHistoryDao
import me.fitbod.repetition.data.WorkoutHistoryEntity
import me.fitbod.repetition.repos.WorkoutHistoryRepoImpl
import org.junit.Rule
import org.junit.jupiter.api.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class WorkoutHistoryUseCaseTest {
    @get:Rule
    var dispatcherRule = CoroutineTestRule()

    private val dispatcherProvider = object : DispatcherProvider {
        override fun default(): CoroutineDispatcher = dispatcherRule.testDispatcher
        override fun io(): CoroutineDispatcher = dispatcherRule.testDispatcher
        override fun main(): CoroutineDispatcher = dispatcherRule.testDispatcher
    }

    private val dao = mockk<WorkoutHistoryDao>()

    private val calculator = BrzyckiOneRmCalculator()
    private val repo = WorkoutHistoryRepoImpl(dao)

    private val useCase = WorkoutHistoryUseCase(dispatcherProvider, repo, calculator)

    @Test
    fun `when no entities, then records size should be 0`() = dispatcherRule.testDispatcher.runBlockingTest {
        // given
        coEvery { dao.getWorkoutHistoryDistinctUntilChanged() } returns flow {
            emit(emptyList<WorkoutHistoryEntity>())
        }

        // when
        val records: Flow<List<WorkoutRecord>> = useCase.getWorkoutOneRmRecords()

        // then
        records.collect { data ->
            data.size.shouldBe(0)
        }
    }

    @Test
    fun `when entities, then records should match`() = dispatcherRule.testDispatcher.runBlockingTest {
        // given
        val entity1 = WorkoutHistoryEntity(LocalDate.of(2020, 10, 11), "exercise1", 1, 4, 40)
        val entity3 = WorkoutHistoryEntity(LocalDate.of(2020, 10, 12), "exercise1", 1, 6, 60)
        val entity2 = WorkoutHistoryEntity(LocalDate.of(2020, 10, 13), "exercise2", 1, 8, 50)
        val entity4 = WorkoutHistoryEntity(LocalDate.of(2020, 10, 14), "exercise2", 1, 10, 80)

        coEvery { dao.getWorkoutHistoryDistinctUntilChanged() } returns flow {
            emit(listOf(entity1, entity2, entity3, entity4))
        }

        // when
        val records: Flow<List<WorkoutRecord>> = useCase.getWorkoutOneRmRecords()

        // then
        records.collect { data ->
            data.size.shouldBe(2)
            data[0].apply {
                exerciseName.shouldBe("exercise1")
                maxOneRm.shouldBe(70)
            }
            data[1].apply {
                exerciseName.shouldBe("exercise2")
                maxOneRm.shouldBe(107)
            }
        }
    }

    @Test
    fun `when getting entities for given exercise, exercises should match`() =
        dispatcherRule.testDispatcher.runBlockingTest {
            // given
            val entity1 = WorkoutHistoryEntity(LocalDate.of(2020, 10, 11), "exercise1", 1, 4, 40)
            val entity3 = WorkoutHistoryEntity(LocalDate.of(2020, 10, 12), "exercise1", 1, 6, 60)
            val entity2 = WorkoutHistoryEntity(LocalDate.of(2020, 10, 11), "exercise2", 1, 8, 50)
            val entity4 = WorkoutHistoryEntity(LocalDate.of(2020, 10, 12), "exercise2", 1, 10, 90)

            coEvery { dao.getWorkoutHistoryDistinctUntilChanged("exercise1") } returns flow {
                emit(listOf(entity1, entity2, entity3, entity4))
            }

            // when
            val exercises: Flow<List<ExerciseHistory>> = useCase.getExerciseHistory("exercise1")

            // then
            exercises.collect { data ->
                data.size.shouldBe(2)
                data[0].apply {
                    exerciseDate.shouldBe(LocalDate.of(2020, 10, 11))
                    oneRm.shouldBe(62)
                }
                data[1].apply {
                    exerciseDate.shouldBe(LocalDate.of(2020, 10, 12))
                    oneRm.shouldBe(120)
                }
            }
        }
}