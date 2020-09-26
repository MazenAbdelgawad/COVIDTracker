package iti.intake40.covidtracker

import androidx.work.WorkManager
import iti.intake40.covidtracker.model.dataModule
import iti.intake40.covidtracker.viewmodel.viewModelModule
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.dsl.module

val managersModule = module {
    single { WorkManager.getInstance(get()) }
}

val covidTrackerModules = listOf(
    dataModule,
    viewModelModule,
    managersModule
)

inline fun <reified T>injectByKoinInstance():T{
    return object : KoinComponent{
        val value : T by inject()
    }.value
}