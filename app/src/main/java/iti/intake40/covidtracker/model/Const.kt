package iti.intake40.covidtracker.model

import android.content.SharedPreferences

class Const {

    companion object{
        public  val PREF_NAME = "covid19_pref"
        public  val PREF_HORE = "houres"
        public  val PREF_DAY = "day"
        public  val PREF_MONTH = "month"
        public  val PREF_YEAR = "year"
        val PREF_COUNTRY_CASES="cases"
        val PREF_COUNTRY_DEATHS="deaths"
        val PREF_COUNTRY_RECOVERED="totalRecovered"

        val KEY_Notification = "KEY_Notification"
        val WORK_MANAGER_TAG = "WorkManagerTag"
        val NOTIFICATION_DATA = "NOTIFICATION_DATA"
    }

}


enum class NotificationHour(val hour: Int) {
    ONE(1),
    TWO(2),
    FIVE(5),
    DAY(24)
}

enum class UpdateDATE{
    hour, year, month
}
