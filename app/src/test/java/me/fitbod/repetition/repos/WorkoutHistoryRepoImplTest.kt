package me.fitbod.repetition.repos

import io.kotest.matchers.collections.shouldNotBeEmpty
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import me.fitbod.repetition.DispatcherProvider
import me.fitbod.repetition.data.WorkoutHistoryDao
import me.fitbod.repetition.data.WorkoutHistoryEntity
import org.junit.Rule
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class WorkoutHistoryRepoImplTest {
    @get:Rule
    var testDispatcher = TestCoroutineDispatcher()

    private val testDispatcherProvider = object : DispatcherProvider {
        override fun default(): CoroutineDispatcher = testDispatcher
        override fun io(): CoroutineDispatcher = testDispatcher
        override fun main(): CoroutineDispatcher = testDispatcher
    }

    private val workoutHistoryDao = mockk<WorkoutHistoryDao>()
    private val repo = WorkoutHistoryRepoImpl(workoutHistoryDao, testDispatcherProvider)

    @Test
    fun `ewerer sds`() = runBlockingTest {
        // given
        every { workoutHistoryDao.getWorkoutHistoryDistinctUntilChanged() } returns flow { listOf<WorkoutHistoryEntity>() }

        // when
        repo.getWorkoutHistory().collect {
            it.shouldNotBeEmpty()
        }
    }
}