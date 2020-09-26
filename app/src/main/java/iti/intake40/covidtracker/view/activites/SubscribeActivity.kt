package iti.intake40.covidtracker.view.activites

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import iti.intake40.covidtracker.R
import iti.intake40.covidtracker.model.Country
import iti.intake40.covidtracker.viewmodel.SubscribeViewModel
import kotlinx.android.synthetic.main.activity_subscribe.*
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

        subscribeViewModel.getCountryfromSharedPref().observe(this, Observer { countryName ->
            if (countryName == null || countryName == "") {
                showSubscribeLayout()
            } else {
                showDetailsLayout(countryName)
            }
        })
    }


    private fun showSubscribeLayout(){
        layout_subscribe.visibility = View.VISIBLE
        layout_details.visibility = View.INVISIBLE
        layout_county_name.visibility = View.INVISIBLE
        imageView_home_heart.visibility = View.INVISIBLE
        setTimeing()
    }

    private fun showDetailsLayout(countryName: String){
        layout_subscribe.visibility = View.INVISIBLE
        layout_details.visibility = View.VISIBLE
        layout_county_name.visibility = View.VISIBLE
        imageView_home_heart.visibility = View.VISIBLE

        subscribeViewModel?.getCountry(countryName)?.observe(this, Observer<Country> {
            setupView(it) //TODO: issue: The null because the WorkManager "need refactor"
        })
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

    private fun setTimeing (){
       subscribeViewModel.getTiming().observe(this, Observer {
           year_id.text = it.year
           month_id.text = it.month
           day_id.text = it.day
       })
    }

}
