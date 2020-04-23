package com.bradrodgers.xkcdviewer

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber
import timber.log.Timber.DebugTree

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(homePageViewModelModule, repoModule, retrofitModule, apiModule))
        }
    }
}