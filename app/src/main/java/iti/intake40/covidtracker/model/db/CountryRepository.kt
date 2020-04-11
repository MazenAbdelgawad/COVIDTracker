package iti.intake40.covidtracker.model.db

import android.app.Application
import iti.intake40.covidtracker.model.Country
import iti.intake40.covidtracker.model.net.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class CountryRepository(application: Application): CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var countryDao: CountryDao?

    init {
        val db= CountryDatabase.getDatabase(application)
        countryDao = db?.countryDao()
    }

    fun getCountries()= countryDao?.getCountries()

    fun getCountry(countryName: String)= countryDao?.getCountry(countryName)

    fun deleteAllCountry()= countryDao?.deleteAllCountries()

    fun setCountries(countries: List<Country>){
        launch { setCountriesBG(countries) }
    }
    private suspend fun setCountriesBG(countries: List<Country>){
        withContext(Dispatchers.IO){
            countryDao?.setCountries(countries)
        }
    }


    fun refreshCountriesFromApi(){
        val service = RetrofitClient.makeRetrofitService()
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.loadCountries()

            withContext(Dispatchers.Main){
                try {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            //TestCode
                            print("@@-> statisticTakenAt= : ${response.body()!!.statisticTakenAt}")
                            print("@@-> Country Count= : ${response.body()!!.countriesStat.size}")
                            //
                            setCountries(response.body()!!.countriesStat)
                        }
                    } else {
                        //TODO: error message can't get data
                        //error message can't get data
                        print("@@-> //error message can't get data")
                    }
                }catch (e: HttpException){
                    print("@@-> //error HttpException")
                }catch (e: Throwable){
                    print("@@-> //error Throwable")
                }
            }

        }

    }




}