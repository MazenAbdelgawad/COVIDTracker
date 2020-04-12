package iti.intake40.covidtracker.view.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import iti.intake40.covidtracker.R
import iti.intake40.covidtracker.model.Country
import iti.intake40.covidtracker.view.adapters.HomeAdapter
import iti.intake40.covidtracker.viewmodel.CountriesViewModel
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private var countriesViewModel: CountriesViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        countriesViewModel = ViewModelProviders.of(this).get(CountriesViewModel::class.java)

        countriesViewModel?.refreshCountriesFromApi()

        countriesViewModel?.getCountries()?.observe(this, Observer<List<Country>> {
            setupView(it)
        })

    }


    fun setupView(list: List<Country>) {
        val layout = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layout
        recyclerView.adapter = HomeAdapter(list)
    }


}
