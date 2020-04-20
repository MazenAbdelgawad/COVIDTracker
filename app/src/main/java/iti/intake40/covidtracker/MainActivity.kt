package iti.intake40.covidtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import iti.intake40.covidtracker.model.Country
import iti.intake40.covidtracker.viewmodel.CountriesViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var countriesViewModel: CountriesViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countriesViewModel = ViewModelProviders.of(this).get(CountriesViewModel::class.java)

        //countriesViewModel?.refreshCountriesFromApi()

        //messageViewModel?.getMessages()?.observe(this, Observer<List<Message>> { this.renderMessges(it) })

        countriesViewModel?.getCountries()?.observe(this,Observer<List<Country>> {
            this.txt_Main.text = it.toString()
        })
    }

    fun setupView(list: List<Country>){
        this.txt_Main.text = list.toString()
    }


}
