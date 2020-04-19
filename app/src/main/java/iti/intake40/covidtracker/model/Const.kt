package iti.intake40.covidtracker.model

import android.content.SharedPreferences

class Const {

    companion object{
        public  val PREF_NAME = "covid19_pref"
        public  val PREF_HORE = "houres"
        public  val PREF_DAY = "day"
        public  val PREF_MONTH = "month"
        public  val PREF_YEAR = "year"

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
