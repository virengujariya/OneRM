package me.fitbod.repetition.db

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty
import me.fitbod.repetition.mappers.LineToWorkoutHistoryEntityMapper
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PrepopulateWorkoutHistoryImplTest {
    lateinit var prepopulateWorkoutHistory: PrepopulateWorkoutHistory
    private val mapper = LineToWorkoutHistoryEntityMapper()

    @Test
    fun workoutDataIsLoadedIgnoringInvalidLines() {
        // given
        val context = ApplicationProvider.getApplicationContext<Context>()
        prepopulateWorkoutHistory = PrepopulateWorkoutHistoryImpl(mapper)

        // when
        val data = prepopulateWorkoutHistory.createFromStream(context.resources.assets.open("workout-test-data.txt"))

        // then
        data.size.shouldBe(6)
        data.forEach {
            it.exerciseName.shouldNotBeEmpty()
            it.reps.shouldBeGreaterThan(0)
            it.sets.shouldBeGreaterThan(0)
            it.weight.shouldBeGreaterThan(0)
        }
    }
}