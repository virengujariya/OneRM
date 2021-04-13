package me.fitbod.repetition.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import me.fitbod.repetition.CoroutineTestRule
import me.fitbod.repetition.db.OneRmDatabase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class WorkoutHistoryDaoTest {
    lateinit var db: OneRmDatabase
    lateinit var workoutHistoryDao: WorkoutHistoryDao

    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, OneRmDatabase::class.java)
            .setTransactionExecutor(coroutineTestRule.testDispatcher.asExecutor())
            .setQueryExecutor(coroutineTestRule.testDispatcher.asExecutor())
            .build()
        workoutHistoryDao = db.workoutHistoryDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    /**
     * Ref: https://developer.android.com/kotlin/flow/test
     */
    @Test
    fun insertWorkoutHistoryEntities() = coroutineTestRule.testDispatcher.runBlockingTest {
        // given
        val entity1 = WorkoutHistoryEntity(LocalDate.of(2020, 10, 11), "exercise1", 1, 10, 45)
        val entity2 = WorkoutHistoryEntity(LocalDate.of(2020, 10, 12), "exercise2", 1, 10, 135)
        val items = listOf(entity1, entity2)

        // when
        val inserts = workoutHistoryDao.insert(items)

        // then
        inserts.size.shouldBe(2)
        inserts.forEachIndexed { index, l ->
            l.shouldBe(index + 1)
        }
    }

    @Test
    fun selectWorkoutHistoryEntities() = coroutineTestRule.testDispatcher.runBlockingTest {
        // given
        val entity1 = WorkoutHistoryEntity(LocalDate.of(2020, 10, 11), "exercise1", 1, 10, 45)
        val entity2 = WorkoutHistoryEntity(LocalDate.of(2020, 10, 12), "exercise2", 1, 10, 135)
        val items = listOf(entity1, entity2)
        workoutHistoryDao.insert(items)

        // when
        val data = workoutHistoryDao.getWorkoutHistoryDistinctUntilChanged().first()

        // then
        data.size.shouldBe(2)
        data[0].apply {
            id.shouldBe(1)
            exerciseName.shouldBe(entity1.exerciseName)
            exerciseDate.shouldBe(entity1.exerciseDate)
            sets.shouldBe(entity1.sets)
            reps.shouldBe(entity1.reps)
            weight.shouldBe(entity1.weight)
        }
        data[1].apply {
            id.shouldBe(2)
            exerciseName.shouldBe(entity2.exerciseName)
            exerciseDate.shouldBe(entity2.exerciseDate)
            sets.shouldBe(entity2.sets)
            reps.shouldBe(entity2.reps)
            weight.shouldBe(entity2.weight)
        }
    }
}