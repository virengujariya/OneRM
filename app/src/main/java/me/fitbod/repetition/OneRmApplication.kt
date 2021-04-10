package me.fitbod.repetition

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OneRmApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}