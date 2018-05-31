package com.tobiasschuerg.telekom

import android.app.Application
import timber.log.Timber

class TelekomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}