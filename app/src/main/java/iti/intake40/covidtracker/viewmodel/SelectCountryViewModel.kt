package iti.intake40.covidtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import iti.intake40.covidtracker.model.db.CountryRepository

class SelectCountryViewModel(application: Application): AndroidViewModel(application){

    private var repository: CountryRepository = CountryRepository(application)

    fun getAllCountry() = repository.getCountries()

}