package iti.intake40.covidtracker.view.activites

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import iti.intake40.covidtracker.R
import iti.intake40.covidtracker.model.Const
import iti.intake40.covidtracker.model.Country
import iti.intake40.covidtracker.view.adapters.HomeAdapter
import iti.intake40.covidtracker.viewmodel.CountriesViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.day_id
import kotlinx.android.synthetic.main.activity_home.month_id
import kotlinx.android.synthetic.main.activity_home.year_id

class HomeActivity : AppCompatActivity() {
    private var countriesViewModel: CountriesViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        countriesViewModel = ViewModelProviders.of(this).get(CountriesViewModel::class.java)

        countriesViewModel?.refreshCountriesFromApi(applicationContext){
            if (it != null){
                Toast.makeText(applicationContext,it,Toast.LENGTH_LONG).show()
            }
        }

        countriesViewModel?.getCountries()?.observe(this, Observer<List<Country>> {
            setupView(it)
            swipe_refresh_layout.isRefreshing = false
        })

        swipe_refresh_layout.setProgressBackgroundColorSchemeColor(Color.TRANSPARENT)
        swipe_refresh_layout.setColorSchemeResources(R.color.colorAccent)
        swipe_refresh_layout.setOnRefreshListener {
            countriesViewModel?.refreshCountriesFromApi(applicationContext) {
                if (it != null) {
                    Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
                    swipe_refresh_layout.isRefreshing = false
                }
            }
        }
    }


    fun setupView(list: List<Country>) {
        val layout = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layout
        recyclerView.adapter = HomeAdapter(list)
        setTiming()
    }

    fun clickSubscribeCountry(view: View) {
        val intent = Intent(this,SubscribeActivity::class.java)
        startActivity(intent)
    }

    private fun setTiming (){
        val sharedPref: SharedPreferences = getSharedPreferences(Const.PREF_NAME, 0)
        year_id.text = sharedPref.getString(Const.PREF_YEAR,"")
        month_id.text  = sharedPref.getString(Const.PREF_MONTH , "")
        day_id.text = sharedPref.getString(Const.PREF_DAY , "")
    }

}
