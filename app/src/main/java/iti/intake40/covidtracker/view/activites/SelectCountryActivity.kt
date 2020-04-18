package iti.intake40.covidtracker.view.activites

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import iti.intake40.covidtracker.R
import iti.intake40.covidtracker.model.Country
import iti.intake40.covidtracker.view.adapters.HomeAdapter
import iti.intake40.covidtracker.view.adapters.SelectCountryAdapter
import iti.intake40.covidtracker.viewmodel.SelectCountryViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_select_country.*

class SelectCountryActivity : AppCompatActivity() {
    private var selectCountryViewModel:SelectCountryViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_country)

        selectCountryViewModel = ViewModelProviders.of(this).get(SelectCountryViewModel::class.java)

        selectCountryViewModel?.getAllCountry()?.observe(this, Observer<List<Country>> {
            setupView(it)
        })
    }

    fun setupView(list: List<Country>) {
        val layout = LinearLayoutManager(applicationContext)
        recycler_select_country.layoutManager = layout
        recycler_select_country.adapter = SelectCountryAdapter(list){
            Toast.makeText(this,"dddddddddddd", Toast.LENGTH_LONG).show()
            val sharedPref: SharedPreferences = getSharedPreferences(SubscribeActivity.PREF_NAME, 0)
            sharedPref.edit().putString(SubscribeActivity.PREF_NAME,it).commit()
            finish()
        }
    }

}
