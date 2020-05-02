package iti.intake40.covidtracker.view.activites

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import iti.intake40.covidtracker.R
import iti.intake40.covidtracker.model.Const
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        if(intent != null && intent.hasExtra(Const.NOTIFICATION_DATA)){
            val list: Array<String> = intent.getStringArrayExtra(Const.NOTIFICATION_DATA)
            setupView(list)
        }
    }

    fun setupView(data: Array<String>){
        txt_update_cases.text = data[0]
        txt_update_deaths.text = data[1]
        txt_update_recovered.text = data[2]

        txt_new_cases.text = data[3]
        txt_new_deaths.text = data[4]
        txt_new_recovered.text = data[5]

        txt_last_cases.text = data[6]
        txt_last_deaths.text = data[7]
        txt_last_recovered.text = data[8]

        setSharedPrefData()
    }


    private fun setSharedPrefData (){
        val sharedPref: SharedPreferences = getSharedPreferences(Const.PREF_NAME, 0)
        year_id.text = sharedPref.getString(Const.PREF_YEAR,"")
        month_id.text  = sharedPref.getString(Const.PREF_MONTH , "")
        day_id.text = sharedPref.getString(Const.PREF_DAY , "")

        txt_country_name_selected.text = sharedPref.getString(Const.PREF_NAME,"")
    }
}
