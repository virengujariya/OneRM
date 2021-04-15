package me.fitbod.repetition.repos

import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import me.fitbod.repetition.data.WorkoutHistoryDao
import me.fitbod.repetition.data.WorkoutHistoryEntity
import org.junit.Rule
import org.junit.jupiter.api.Test
import java.time.LocalDate

@ExperimentalCoroutinesApi
class WorkoutHistoryRepoImplTest {
    @get:Rule
    var testDispatcher = TestCoroutineDispatcher()

    private val workoutHistoryDao = mockk<WorkoutHistoryDao>()
    private val repo = WorkoutHistoryRepoImpl(workoutHistoryDao)

    @Test
    fun `when getting workout history, then it should return successful result`() = testDispatcher.runBlockingTest {
        // given
        val entities: List<WorkoutHistoryEntity> = listOf(
            WorkoutHistoryEntity(LocalDate.of(2020, 10, 11), "exercise1", 1, 10, 140),
            WorkoutHistoryEntity(LocalDate.of(2020, 10, 12), "exercise2", 1, 4, 100)
        )
        every { workoutHistoryDao.getWorkoutHistoryDistinctUntilChanged() } returns flowOf(entities)

        // when
        repo.getWorkoutHistory().collect { data ->
            data.size.shouldBe(2)
        }
    }

    @Test
    fun `when getting workout history by exercise name, then it should return successful result`() =
        testDispatcher.runBlockingTest {
            // given
            val entities: List<WorkoutHistoryEntity> = listOf(
                WorkoutHistoryEntity(LocalDate.of(2020, 10, 12), "exercise2", 1, 4, 100),
                WorkoutHistoryEntity(LocalDate.of(2020, 10, 13), "exercise2", 1, 6, 80),
            )
            every { workoutHistoryDao.getWorkoutHistoryDistinctUntilChanged("exercise2") } returns flowOf(entities)

            // when
            repo.getWorkoutHistory("exercise2").collect { data ->
                data.size.shouldBe(2)
                data.forEach { it.exerciseName.shouldBe("exercise2") }
            }
        }
}