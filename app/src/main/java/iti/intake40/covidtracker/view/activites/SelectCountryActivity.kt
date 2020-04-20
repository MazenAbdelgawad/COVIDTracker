package iti.intake40.covidtracker.view.activites

import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock.sleep
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import iti.intake40.covidtracker.R
import iti.intake40.covidtracker.model.Const
import iti.intake40.covidtracker.model.Const.Companion.KEY_Notification
import iti.intake40.covidtracker.model.Const.Companion.WORK_MANAGER_TAG
import iti.intake40.covidtracker.model.Country
import iti.intake40.covidtracker.model.NotificationHour
import iti.intake40.covidtracker.util.NotificationWork
import iti.intake40.covidtracker.view.adapters.SelectCountryAdapter
import iti.intake40.covidtracker.viewmodel.SelectCountryViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_select_country.*
import kotlinx.android.synthetic.main.activity_select_country.day_id
import kotlinx.android.synthetic.main.activity_select_country.month_id
import kotlinx.android.synthetic.main.activity_select_country.txt_country_name_selected
import kotlinx.android.synthetic.main.activity_select_country.year_id
import java.util.concurrent.TimeUnit


class SelectCountryActivity : AppCompatActivity() {
    private var selectCountryViewModel: SelectCountryViewModel? = null
    private val workManager = WorkManager.getInstance(application)

    var selectRadioButtonHour : Int = 2
    var countryCases:String="0"
    var countryDeaths:String="0"
    var countryRecovered:String="0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_country)

        selectCountryViewModel = ViewModelProviders.of(this).get(SelectCountryViewModel::class.java)
        selectCountryViewModel?.getAllCountry()?.observe(this, Observer<List<Country>> {
            setupView(it)
        })


    }


    private fun setupView(list: List<Country>) {
        val layout = LinearLayoutManager(applicationContext)
        recycler_select_country.layoutManager = layout
        recycler_select_country.adapter = SelectCountryAdapter(list) {
            txt_country_name_selected.text =  it.countryName
            countryCases = it.cases
            countryDeaths = it.deaths
            countryRecovered = it.totalRecovered
        }
        setTimeing()
        setRadioButton()
    }

    fun onRadioButtonClicked(view: View) {

        if (view is RadioButton) {
            val checked = view.isChecked

            when (view.getId()) {
                R.id.rb1 ->
                    if (checked) {
                        print("chked 1")
                        rb2.setChecked(false)
                        rb3.setChecked(false)
                        rb4.setChecked(false)
                        selectRadioButtonHour = 1

                    }

                R.id.rb2 ->
                    if (checked) {
                        rb1.setChecked(false)
                        rb3.setChecked(false)
                        rb4.setChecked(false)
                        selectRadioButtonHour = 2
                    }

                R.id.rb3 ->
                    if (checked) {
                        rb2.setChecked(false)
                        rb1.setChecked(false)
                        rb4.setChecked(false)
                        selectRadioButtonHour = 5
                    }

                R.id.rb4 ->
                    if (checked) {
                        rb2.setChecked(false)
                        rb3.setChecked(false)
                        rb1.setChecked(false)
                        selectRadioButtonHour = 24
                    }
            }


        }
    }

    fun clickBtnSave(view: View) {
        if(txt_country_name_selected.text.trim().isNotEmpty()){
            val editPref: SharedPreferences.Editor = getSharedPreferences(Const.PREF_NAME, 0).edit()
            editPref.putString(Const.PREF_NAME,txt_country_name_selected.text.toString())
            editPref.putInt(Const.PREF_HORE ,selectRadioButtonHour)
            editPref.putString(Const.PREF_COUNTRY_CASES,countryCases)
            editPref.putString(Const.PREF_COUNTRY_DEATHS,countryDeaths)
            editPref.putString(Const.PREF_COUNTRY_RECOVERED,countryRecovered)
            editPref.commit()

            //WorkManager
            startWorkManger()

            finish()
        }else{
            Toast.makeText(this, "Please Select country", Toast.LENGTH_LONG).show()
        }
    }

    fun setTimeing (){
        val sharedPref: SharedPreferences = getSharedPreferences(Const.PREF_NAME, 0)
        year_id.text = sharedPref.getString(Const.PREF_YEAR,"")
        month_id.text  = sharedPref.getString(Const.PREF_MONTH , "")
        day_id.text = sharedPref.getString(Const.PREF_DAY , "")
    }


    fun setRadioButton(){
        val sharedPref: SharedPreferences = getSharedPreferences(Const.PREF_NAME, 0)
        txt_country_name_selected.text = sharedPref.getString(Const.PREF_NAME , "")

        var houre = sharedPref.getInt(Const.PREF_HORE,2)
        selectRadioButtonHour = houre
        when(houre) {
            NotificationHour.ONE.hour -> { rb1.setChecked(true) ; rb2.setChecked(false) ;rb3.setChecked(false);rb4.setChecked(false)}
            NotificationHour.TWO.hour -> { rb1.setChecked(false) ; rb2.setChecked(true) ;rb3.setChecked(false);rb4.setChecked(false) }
            NotificationHour.FIVE.hour -> { rb1.setChecked(false) ; rb2.setChecked(false) ;rb3.setChecked(true);rb4.setChecked(false)}
            NotificationHour.DAY.hour -> { rb1.setChecked(false) ; rb2.setChecked(false) ;rb3.setChecked(false);rb4.setChecked(true)}
        }
    }



    private fun startWorkManger(){
        val notificationRequest = PeriodicWorkRequestBuilder<NotificationWork>(selectRadioButtonHour.toLong(), TimeUnit.HOURS) //TODO: Change with SelectedRadiobuton
            .addTag(WORK_MANAGER_TAG)
            .build()
        workManager.enqueueUniquePeriodicWork(WORK_MANAGER_TAG, ExistingPeriodicWorkPolicy.REPLACE, notificationRequest)
    }

    fun cancelWork() {
        workManager.cancelAllWorkByTag(WORK_MANAGER_TAG)
    }

}