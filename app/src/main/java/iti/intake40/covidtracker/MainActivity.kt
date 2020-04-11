package iti.intake40.covidtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import iti.intake40.covidtracker.viewmodel.CountriesViewModel

class MainActivity : AppCompatActivity() {

    private var countriesViewModel: CountriesViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countriesViewModel = ViewModelProviders.of(this).get(CountriesViewModel::class.java)

        countriesViewModel?.refreshCountriesFromApi()
    }


}
