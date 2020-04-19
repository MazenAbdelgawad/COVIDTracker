package iti.intake40.covidtracker.model.db

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import iti.intake40.covidtracker.model.Const
import iti.intake40.covidtracker.model.Country
import iti.intake40.covidtracker.model.net.RetrofitClient
import kotlinx.android.synthetic.main.activity_select_country.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import kotlin.coroutines.CoroutineContext

class CountryRepository(var application: Application): CoroutineScope {

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
        Log.i("@@->"," Start refreshCountriesFromApi()")
        val service = RetrofitClient.makeRetrofitService()
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.loadCountries()

            withContext(Dispatchers.Main){
                try {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            //TestCode
                            Log.i("@@-> statisticTakenAt= "  , "${response.body()!!.statisticTakenAt}")
                            Log.i("@@-> statisticTakenAt= "  , "ahmed saeed")
                            saveDateInPref(response.body()!!.statisticTakenAt)
                            Log.i("@@-> statisticTakenAt= "  , "mohamed saeed")
                            Log.i("@@-> Country Count= "," ${response.body()!!.countriesStat.size}")

                            var listxx= response.body()!!.countriesStat

                            setCountries(response.body()!!.countriesStat)

                        }else{
                            Log.i("@@-> = ","  if (response.body() != null) = null")
                        }
                    } else {
                        //TODO: error message can't get data
                        //error message can't get data
                        Log.i("@@-> ","//error message can't get data")
                    }
                }catch (e: HttpException){
                    Log.i("@@->"," //error HttpException")
                }catch (e: Throwable){
                    Log.i("@@->"," //error Throwable")
                }
            }

        }

    }


    fun saveDateInPref(date: String){
        launch {
            saveDateInPrefDB(date)
        }
    }

    suspend fun saveDateInPrefDB(date:String) {

        Log.i("@@->"," start function")

        var realDate = date.replaceAfter(" ", " ", " ")
        var splitDate = realDate.split("-")

        val year = splitDate[0]
        val monthNumber = splitDate[1].toInt()
        val day = splitDate[2]
        var month: String = ""

        when(monthNumber) {
            1 -> month = "Jan"
            2 -> month = "Feb"
            3 -> month = "March"
            4 -> month = "April"
            5 -> month = "May"
            6 -> month = "June"
            7 -> month = "July"
            8 -> month = "August"
            9 -> month = "Sep"
            10 -> month = "Oct"
            11 -> month = "Nov"
            12 -> month = "Dec"
        }

        val sharedPref: SharedPreferences = application.getSharedPreferences(Const.PREF_NAME ,0)
        sharedPref.edit().putString(Const.PREF_YEAR, year ).commit()
        sharedPref.edit().putString (Const.PREF_MONTH ,month ).commit()
        sharedPref.edit().putString(Const.PREF_DAY ,day).commit()

    }



}