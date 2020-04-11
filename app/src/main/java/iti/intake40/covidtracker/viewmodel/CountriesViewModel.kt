package iti.intake40.covidtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import iti.intake40.covidtracker.model.Country
import iti.intake40.covidtracker.model.db.CountryRepository
import kotlinx.coroutines.launch

class CountriesViewModel(application: Application) : AndroidViewModel(application) {

    private var repository:CountryRepository = CountryRepository(application)

    fun getCountries()= repository.getCountries()

    //fun getCountry(countryName: String)= repository.getCountry(countryName)

    fun refreshCountriesFromApi() = repository.refreshCountriesFromApi()

}