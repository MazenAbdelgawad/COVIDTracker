package iti.intake40.covidtracker.model


import android.content.SharedPreferences
import iti.intake40.covidtracker.injectByKoinInstance

class Timing(
    val year: String,
    val month: String,
    val day: String
)

suspend fun sharedPrefGetTiming(): Timing {
    val sharedPref: SharedPreferences = injectByKoinInstance()
    return Timing(
        year = sharedPref.getString(Const.PREF_YEAR, "") ?: "",
        month = sharedPref.getString(Const.PREF_MONTH, "") ?: "",
        day = sharedPref.getString(Const.PREF_DAY, "") ?: ""
    )
}