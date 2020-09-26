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

class SelectCountryViewModel(application: Application, private val repository: CountryRepository): AndroidViewModel(application){

//    private var repository: CountryRepository =
//        CountryRepository(application)

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

    fun getHourFromSharedPref(): LiveData<Int> {
        val hour = MutableLiveData<Int>()
        viewModelScope.launch(Dispatchers.IO) {
            val sharedPref: SharedPreferences = injectByKoinInstance()
            var data = sharedPref.getInt(Const.PREF_HORE,2)
            withContext(Dispatchers.Main) {
                hour.postValue(data)
            }
        }
        return hour
    }

    fun saveSelectedCountryData(
        countryName: String,
        selectRadioButtonHour: Int,
        countryCases: String,
        countryDeaths: String,
        countryRecovered: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val editPref: SharedPreferences.Editor = injectByKoinInstance<SharedPreferences>().edit()
            editPref.putString(Const.PREF_NAME, countryName)
            editPref.putInt(Const.PREF_HORE, selectRadioButtonHour)
            editPref.putString(Const.PREF_COUNTRY_CASES, countryCases)
            editPref.putString(Const.PREF_COUNTRY_DEATHS, countryDeaths)
            editPref.putString(Const.PREF_COUNTRY_RECOVERED, countryRecovered)
            editPref.apply()
        }
    }

}