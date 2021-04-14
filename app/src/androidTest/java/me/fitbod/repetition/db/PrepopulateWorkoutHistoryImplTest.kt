package me.fitbod.repetition.db

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PrepopulateWorkoutHistoryImplTest {
    lateinit var prepopulateWorkoutHistory: PrepopulateWorkoutHistory

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // prepopulateWorkoutHistory =
        //     PrepopulateWorkoutHistoryImpl(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun sdsd() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.resources.assets.open("workout-test-data.txt")
        // val workouts = prepopulateWorkoutHistory.createFromAssets("workout-test-data.txt")
        // workouts.size.shouldBe(10)
    }

    @Test
    fun mapToEntity() {
        val entity = "ds"
    }
}