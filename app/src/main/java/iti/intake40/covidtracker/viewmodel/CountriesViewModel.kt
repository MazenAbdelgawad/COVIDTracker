package iti.intake40.covidtracker.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import iti.intake40.covidtracker.model.CountryRepository
import iti.intake40.covidtracker.model.net.NetworkUtil
import org.koin.core.KoinComponent
import org.koin.core.inject

class CountriesViewModel(application: Application, private val repository: CountryRepository) : AndroidViewModel(application) {

//    private var repository: CountryRepository =
//        CountryRepository(application)

    fun getCountries()= repository.getCountries()

    //fun getCountry(countryName: String)= repository.getCountry(countryName)

    fun refreshCountriesFromApi(appContext: Context, showError: (String?)->Unit ) {
        if(NetworkUtil().isNetworkConnected(appContext)) {
            repository.refreshCountriesFromApi()
            showError(null)
        }else{
            showError("Check network connection to be update..!")
        }
    }

}