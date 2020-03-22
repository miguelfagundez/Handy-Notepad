package com.devproject.fagundezdev.handynotepad

import android.app.Application
import timber.log.Timber

class HandyNotepadApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}