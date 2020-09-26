package iti.intake40.covidtracker.viewmodel

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { CountriesViewModel(get(), get()) }
    viewModel { SubscribeViewModel(get(), get()) }
    viewModel { SelectCountryViewModel(get(), get()) }
}