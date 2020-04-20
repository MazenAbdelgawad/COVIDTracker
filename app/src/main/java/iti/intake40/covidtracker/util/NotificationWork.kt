package iti.intake40.covidtracker.util

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import iti.intake40.covidtracker.model.Const
import iti.intake40.covidtracker.model.db.CountryDao
import iti.intake40.covidtracker.model.db.CountryDatabase
import iti.intake40.covidtracker.model.db.CountryRepository
import iti.intake40.covidtracker.model.net.NetworkUtil
import iti.intake40.covidtracker.model.net.RetrofitClient
import iti.intake40.workmanager_notification_demo.util.sendNotification
import kotlinx.android.synthetic.main.activity_select_country.*
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
            var repository = CountryRepository(appContext)
            val service = RetrofitClient.makeRetrofitService()
            val response = service.loadCountries()

            withContext(Dispatchers.IO) {
                try {
                    if (response.isSuccessful) {
                        if (response.body() != null) {

                            val db = CountryDatabase.getDatabase(applicationContext)
                            db?.countryDao()?.setCountries(response.body()!!.countriesStat)

                            var msg = ""
                            val sharedPref: SharedPreferences =
                                appContext.getSharedPreferences(Const.PREF_NAME, 0)
                            val countryName = sharedPref.getString(Const.PREF_NAME, "")
                            var countryCases = sharedPref.getString(Const.PREF_COUNTRY_CASES, "0")
                            var countryDeaths = sharedPref.getString(Const.PREF_COUNTRY_DEATHS, "0")
                            var countryRecovered =
                                sharedPref.getString(Const.PREF_COUNTRY_RECOVERED, "0")

                            if (countryName != null) {
                                msg += "$countryName have "
                                var country = repository.getCountryObj(countryName)
                                var changeFlag = false

                                //ToDo:Make all Not Equal to Be Crroect logic
                                if (country != null) {
                                    if (!country.cases.equals(countryCases)) {
                                        msg += "${country.cases.replace(",", "", true)
                                            .toInt() - countryCases!!.replace(",", "", true)
                                            .toInt()} new Cases,"
                                        changeFlag = true
                                    }
                                    if (!country.deaths.equals(countryDeaths)) {
                                        msg += " ${country.deaths.replace(",", "", true)
                                            .toInt() - countryDeaths!!.replace(",", "", true)
                                            .toInt()} new Deaths,"
                                        changeFlag = true
                                    }
                                    if (!country.totalRecovered.equals(countryRecovered)) {
                                        msg += " ${country.totalRecovered.replace(",", "", true)
                                            .toInt() - countryRecovered!!.replace(",", "", true)
                                            .toInt()} new Recovered."
                                        changeFlag = true
                                    }

                                    if (changeFlag == true) {

                                        val editPref: SharedPreferences.Editor =
                                            appContext.getSharedPreferences(Const.PREF_NAME, 0)
                                                .edit()
                                        editPref.putString(Const.PREF_COUNTRY_CASES, countryCases)
                                        editPref.putString(Const.PREF_COUNTRY_DEATHS, countryDeaths)
                                        editPref.putString(
                                            Const.PREF_COUNTRY_RECOVERED,
                                            countryRecovered
                                        )
                                        editPref.commit()

                                        val notificationManager = ContextCompat.getSystemService(
                                            appContext,
                                            NotificationManager::class.java
                                        ) as NotificationManager
                                        notificationManager.sendNotification(msg, appContext)
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