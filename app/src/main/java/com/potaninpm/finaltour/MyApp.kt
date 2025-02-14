package com.potaninpm.finaltour

import android.app.Application
import com.potaninpm.finaltour.di.coreModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApp)
            modules(coreModules)
        }

    }
}