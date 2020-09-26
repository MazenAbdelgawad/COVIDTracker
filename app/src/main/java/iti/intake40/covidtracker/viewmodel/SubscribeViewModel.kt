package iti.intake40.covidtracker.viewmodel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import iti.intake40.covidtracker.injectByKoinInstance
import iti.intake40.covidtracker.model.Const
import iti.intake40.covidtracker.model.CountryRepository
import iti.intake40.covidtracker.model.Timing
import iti.intake40.covidtracker.model.sharedPrefGetTiming
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubscribeViewModel(application: Application,private val repository: CountryRepository) : AndroidViewModel(application) {

//    private var repository: CountryRepository =
//        CountryRepository(application)

    fun getCountry(countryName: String)= repository.getCountry(countryName)

    fun getAllCountry() = repository.getCountries()

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

    fun getCountryfromSharedPref(): LiveData<String?> {
        val name = MutableLiveData<String>()
        viewModelScope.launch(Dispatchers.IO) {
            val sharedPref: SharedPreferences = injectByKoinInstance()
            val countryName = sharedPref.getString(Const.PREF_NAME, "")
            withContext(Dispatchers.Main) {
                name.postValue(countryName)
            }
        }
        return name
    }
}