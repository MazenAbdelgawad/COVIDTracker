package iti.intake40.covidtracker.util

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import iti.intake40.covidtracker.model.Const
import iti.intake40.covidtracker.model.CountryRepository
import iti.intake40.covidtracker.model.db.CountryDatabase
import iti.intake40.covidtracker.model.net.NetworkUtil
import iti.intake40.covidtracker.model.net.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException


class NotificationWork(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        if (NetworkUtil().isNetworkConnected(applicationContext)) {
            makeNotification(applicationContext)
        }

        return Result.success()
    }

    private fun makeNotification(appContext: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            var repository =
                CountryRepository(appContext)
            val service = RetrofitClient.makeRetrofitService()
            val response = service.loadCountries()

            withContext(Dispatchers.IO) {
                try {
                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            val filterCountries = response.body()!!.countriesStat.filter { it.countryName.trim() != ""  }

                            val db = CountryDatabase.getDatabase(applicationContext)
                            db?.countryDao()?.deleteAllCountries()
                            db?.countryDao()?.setCountries(filterCountries)

                            CountryRepository(applicationContext).saveDateInPref(response.body()!!.statisticTakenAt)

                            var msg = ""
                            var updateCases = 0
                            var updateDeaths = 0
                            var updateRecovered = 0
                            val sharedPref: SharedPreferences =
                                appContext.getSharedPreferences(Const.PREF_NAME, 0)
                            val countryName = sharedPref.getString(Const.PREF_NAME, "")
                            var oldCountryCases = sharedPref.getString(Const.PREF_COUNTRY_CASES, "0")
                            var oldCountryDeaths = sharedPref.getString(Const.PREF_COUNTRY_DEATHS, "0")
                            var oldCountryRecovered =
                                sharedPref.getString(Const.PREF_COUNTRY_RECOVERED, "0")

                            if (countryName != null) {
                                msg += "$countryName have "
                                var country = repository.getCountryObj(countryName)
                                var changeFlag = false

                                //ToDo:Make all Not Equal to Be Crroect logic
                                if (country != null) {
                                    if (!country.cases.equals(oldCountryCases)) {
                                        updateCases =  country.cases.replace(",", "", true)
                                                .toInt() - oldCountryCases!!.replace(",", "", true).toInt()
                                        msg += " $updateCases new Cases,"
                                        changeFlag = true
                                    }
                                    if (!country.deaths.equals(oldCountryDeaths)) {
                                        updateDeaths = country.deaths.replace(",", "", true)
                                            .toInt() - oldCountryDeaths!!.replace(",", "", true).toInt()
                                        msg += " $updateDeaths new Deaths,"
                                        changeFlag = true
                                    }
                                    if (!country.totalRecovered.equals(oldCountryRecovered)) {
                                        updateRecovered = country.totalRecovered.replace(",", "", true)
                                            .toInt() - oldCountryRecovered!!.replace(",", "", true).toInt()
                                        msg += " $updateRecovered new Recovered."
                                        changeFlag = true
                                    }

                                    if (changeFlag == true) {

                                        val editPref: SharedPreferences.Editor =
                                            appContext.getSharedPreferences(Const.PREF_NAME, 0)
                                                .edit()
                                        editPref.putString(Const.PREF_COUNTRY_CASES, country.cases)
                                        editPref.putString(Const.PREF_COUNTRY_DEATHS, country.deaths)
                                        editPref.putString(
                                            Const.PREF_COUNTRY_RECOVERED,
                                            country.totalRecovered
                                        )
                                        editPref.commit()

                                        val notificationManager = ContextCompat.getSystemService(
                                            appContext,
                                            NotificationManager::class.java
                                        ) as NotificationManager

                                        /////////////////////////////////////////////
                                        //more details of Notification
//                                        var s = "\n ------------------ \n new (net): " +
//                                                "Cases= ${country.cases}, deaths= ${country.deaths}, Recoverd= ${country.totalRecovered}, " +
//                                                "\n ------------------ \n old (Pref): " +
//                                                " pCases= ${oldCountryCases}, pdeaths= ${oldCountryDeaths}, pRecoverd= ${oldCountryRecovered}"
//
//                                        msg += s
                                        //////////////////////////////////////////////

                                        var notif_data: Array<String> = arrayOf(
                                            updateCases.toString(),updateDeaths.toString(),updateRecovered.toString(),
                                            country.cases,country.deaths,country.totalRecovered,
                                            oldCountryCases,oldCountryDeaths,oldCountryRecovered)

                                            notificationManager.sendNotification(msg, notif_data, appContext)
                                    } else {
                                        Log.i("@@-> flage ", "  changeFlag False")
                                    }

                                } else {
                                    Log.i("@@->**** ERORR if ", "  if (country!= null)")
                                }
                            } else {
                                Log.i("@@->**** ERORR if ", "  if (countryName!= null)")
                            }

                        } else {
                            Log.i("@@-> = ", "  if (response.body() != null) = null")
                        }
                    } else {
                        //TODO: error message can't get data
                        //error message can't get data
                        Log.i("@@-> ", "//error message can't get data")
                    }

                } catch (e: HttpException) {
                    Log.i("@@->", " NW//error HttpException")
                } catch (e: Throwable) {
                    Log.i("@@->", " NW//error Throwable")
                }
            }
        }

    }


}