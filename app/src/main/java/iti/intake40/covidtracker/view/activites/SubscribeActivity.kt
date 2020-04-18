package iti.intake40.covidtracker.view.activites

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import iti.intake40.covidtracker.R
import iti.intake40.covidtracker.model.Country
import iti.intake40.covidtracker.viewmodel.SubscribeViewModel
import kotlinx.android.synthetic.main.activity_subscribe.*
import kotlinx.android.synthetic.main.activity_subscribe.txt_country_name

class SubscribeActivity : AppCompatActivity() {
    companion object{
        public  val PREF_NAME = "covid19_pref"
    }


    private var subscribeViewModel:SubscribeViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscribe)

        subscribeViewModel = ViewModelProviders.of(this).get(SubscribeViewModel::class.java)

    }

    override fun onStart() {
        super.onStart()

        val sharedPref: SharedPreferences = getSharedPreferences(PREF_NAME, 0)
        val countryName = sharedPref.getString(PREF_NAME, "")
        if (countryName != null) {
            if (countryName.equals("")){
                layout_subscribe.visibility = View.VISIBLE
                layout_details.visibility = View.INVISIBLE
                txt_country_name.visibility = View.INVISIBLE
                imageView_home_heart.visibility = View.INVISIBLE
            } else {
                layout_subscribe.visibility = View.INVISIBLE
                layout_details.visibility = View.VISIBLE
                txt_country_name.visibility = View.VISIBLE
                imageView_home_heart.visibility = View.VISIBLE

                subscribeViewModel?.getCountry(countryName)?.observe(this,Observer<Country>{
                    setupView(it)
                })
            }
        }

    }


    fun setupView(country: Country){
        txt_country_name.text = country.countryName
        txt_cases.text = country.cases
        txt_new_cases.text = country.newCases
        txt_total_recovered.text = country.totalRecovered
        txt_active_cases.text = country.activeCases
        txt_deaths.text = country.deaths
        txt_new_deaths.text = country.newDeaths
        txt_serious_critical.text = country.seriousCritical
        txt_cases_per_million.text = country.totalCasesPer1MPopulation
    }


    fun btn_subscribe_click(view: View) {
        val intent = Intent(this,SelectCountryActivity::class.java)
        startActivity(intent)
    }

}
