package iti.intake40.covidtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import iti.intake40.covidtracker.model.CountryRepository

class SubscribeViewModel(application: Application,private val repository: CountryRepository) : AndroidViewModel(application) {

//    private var repository: CountryRepository =
//        CountryRepository(application)

    fun getCountry(countryName: String)= repository.getCountry(countryName)

    fun getAllCountry() = repository.getCountries()
}