package me.fitbod.repetition

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import me.fitbod.repetition.db.OneRmDatabase
import javax.inject.Inject

@HiltAndroidApp
class OneRmApplication : Application() {
    @Inject
    lateinit var db: OneRmDatabase

    override fun onCreate() {
        super.onCreate()
    }
}