package iti.intake40.covidtracker.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import iti.intake40.covidtracker.model.CountryRepository
import iti.intake40.covidtracker.model.Timing
import iti.intake40.covidtracker.model.net.NetworkUtil
import iti.intake40.covidtracker.model.sharedPrefGetTiming
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    fun getTiming(): LiveData<Timing> {
        val timing = MutableLiveData<Timing>()
        viewModelScope.launch(Dispatchers.IO) {
            val data= sharedPrefGetTiming()
            withContext(Dispatchers.Main) {
                timing.postValue(data)
            }
        }
        return timing
    }

}