package iti.intake40.covidtracker

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CovidTrackerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            printLogger(Level.DEBUG)
            androidContext(this@CovidTrackerApp)
            modules(covidTrackerModule)
        }
    }
}