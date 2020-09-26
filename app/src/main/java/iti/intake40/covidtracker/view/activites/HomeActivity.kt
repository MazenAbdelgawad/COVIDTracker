package iti.intake40.covidtracker.view.activites

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import iti.intake40.covidtracker.R
import iti.intake40.covidtracker.model.Country
import iti.intake40.covidtracker.view.adapters.HomeAdapter
import iti.intake40.covidtracker.viewmodel.CountriesViewModel
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {
    private val countriesViewModel: CountriesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //countriesViewModel = ViewModelProviders.of(this).get(CountriesViewModel::class.java)

        setupView()

        countriesViewModel?.refreshCountriesFromApi(applicationContext){
            if (it != null){
                Toast.makeText(applicationContext,it,Toast.LENGTH_LONG).show()
            }
        }

        countriesViewModel?.getCountries()?.observe(this, Observer<List<Country>> {
            updateView(it)
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


    private fun setupView() {
        val layout = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layout
        recyclerView.adapter = HomeAdapter(listOf())
        setTiming()
    }

    private fun updateView(list: List<Country>) {
        recyclerView.adapter = HomeAdapter(list)
        setTiming()
    }

    fun clickSubscribeCountry(view: View) {
        val intent = Intent(this,SubscribeActivity::class.java)
        startActivity(intent)
    }

    private fun setTiming() {
        countriesViewModel.getTiming().observe(this, Observer {
            year_id.text = it.year
            month_id.text = it.month
            day_id.text = it.day
        })
    }

}
