package iti.intake40.covidtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import iti.intake40.covidtracker.model.db.CountryRepository

class SubscribeViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: CountryRepository = CountryRepository(application)

    fun getCountry(countryName: String)= repository.getCountry(countryName)

    fun getAllCountry() = repository.getCountries()
}