package iti.intake40.covidtracker

import iti.intake40.covidtracker.viewmodel.viewModelModule
import org.koin.core.module.Module
import org.koin.dsl.module

val covidTrackerModules = listOf(viewModelModule)