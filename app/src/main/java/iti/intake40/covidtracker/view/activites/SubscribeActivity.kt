package iti.intake40.covidtracker.view.activites

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import iti.intake40.covidtracker.R
import iti.intake40.covidtracker.model.Const
import iti.intake40.covidtracker.model.Country
import iti.intake40.covidtracker.viewmodel.SubscribeViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_subscribe.*
import kotlinx.android.synthetic.main.activity_subscribe.day_id
import kotlinx.android.synthetic.main.activity_subscribe.imageView_home_heart
import kotlinx.android.synthetic.main.activity_subscribe.month_id
import kotlinx.android.synthetic.main.activity_subscribe.txt_country_name_selected
import kotlinx.android.synthetic.main.activity_subscribe.year_id
import org.koin.android.ext.android.inject

class SubscribeActivity : AppCompatActivity() {



    private val subscribeViewModel:SubscribeViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscribe)

        //subscribeViewModel = ViewModelProviders.of(this).get(SubscribeViewModel::class.java)

    }

    override fun onStart() {
        super.onStart()

        val sharedPref: SharedPreferences = getSharedPreferences(Const.PREF_NAME, 0)
        val countryName = sharedPref.getString(Const.PREF_NAME, "")
        if (countryName != null) {
            if (countryName.equals("")){
                layout_subscribe.visibility = View.VISIBLE
                layout_details.visibility = View.INVISIBLE
                layout_county_name.visibility = View.INVISIBLE
                imageView_home_heart.visibility = View.INVISIBLE
                setTimeing()
            } else {
                layout_subscribe.visibility = View.INVISIBLE
                layout_details.visibility = View.VISIBLE
                layout_county_name.visibility = View.VISIBLE
                imageView_home_heart.visibility = View.VISIBLE

                subscribeViewModel?.getCountry(countryName)?.observe(this,Observer<Country>{
                    setupView(it)
                })
            }
        }

    }


    fun setupView(country: Country){
        txt_country_name_selected.text = country.countryName
        txt_cases.text = country.cases
        txt_new_cases.text = country.newCases
        txt_total_recovered.text = country.totalRecovered
        txt_active_cases.text = country.activeCases
        txt_deaths.text = country.deaths
        txt_new_deaths.text = country.newDeaths
        txt_serious_critical.text = country.seriousCritical
        txt_cases_per_million.text = country.totalCasesPer1MPopulation
        setTimeing()
    }


    fun btn_subscribe_click(view: View) {
        val intent = Intent(this,SelectCountryActivity::class.java)
        startActivity(intent)

    }
    fun setTimeing (){
        val sharedPref: SharedPreferences = getSharedPreferences(Const.PREF_NAME, 0)
        year_id.text = sharedPref.getString(Const.PREF_YEAR,"")
        month_id.text  = sharedPref.getString(Const.PREF_MONTH , "")
        day_id.text = sharedPref.getString(Const.PREF_DAY , "")
    }
}
