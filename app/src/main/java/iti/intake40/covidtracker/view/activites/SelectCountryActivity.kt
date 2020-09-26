package iti.intake40.covidtracker.view.activites

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import iti.intake40.covidtracker.R
import iti.intake40.covidtracker.model.Const.Companion.WORK_MANAGER_TAG
import iti.intake40.covidtracker.model.Country
import iti.intake40.covidtracker.model.NotificationHour
import iti.intake40.covidtracker.util.NotificationWork
import iti.intake40.covidtracker.view.adapters.SelectCountryAdapter
import iti.intake40.covidtracker.viewmodel.SelectCountryViewModel
import kotlinx.android.synthetic.main.activity_select_country.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class SelectCountryActivity : AppCompatActivity() {
    private val selectCountryViewModel: SelectCountryViewModel by viewModel()
    private val workManager: WorkManager by inject() //= WorkManager.getInstance(application)

    var selectRadioButtonHour : Int = 2
    var countryCases:String="0"
    var countryDeaths:String="0"
    var countryRecovered:String="0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_country)

        //selectCountryViewModel = ViewModelProviders.of(this).get(SelectCountryViewModel::class.java)
        setupView()
        selectCountryViewModel?.getAllCountry()?.observe(this, Observer<List<Country>> {
            updateView(it)
        })


    }


    private fun setupView() {
        val layout = LinearLayoutManager(applicationContext)
        recycler_select_country.layoutManager = layout
        recycler_select_country.adapter = SelectCountryAdapter(listOf(),{})
    }

    private fun updateView(list: List<Country>) {
        recycler_select_country.adapter = SelectCountryAdapter(list) {
            txt_country_name_selected.text =  it.countryName
            countryCases = it.cases
            countryDeaths = it.deaths
            countryRecovered = it.totalRecovered
        }
        setTiming()
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
            selectCountryViewModel.saveSelectedCountryData(
                txt_country_name_selected.text.toString(),
                selectRadioButtonHour,
                countryCases,
                countryDeaths,
                countryRecovered
            )

            //WorkManager
            startWorkManger()

            finish()
        }else{
            Toast.makeText(this, "Please Select country", Toast.LENGTH_LONG).show()
        }
    }

    private fun setTiming() {
        selectCountryViewModel.getTiming().observe(this, Observer {
            year_id.text = it.year
            month_id.text = it.month
            day_id.text = it.day
        })
    }


    fun setRadioButton(){
        selectCountryViewModel.getHourFromSharedPref().observe(this, Observer {
        selectRadioButtonHour = it
            when(it) {
                NotificationHour.ONE.hour -> { rb1.setChecked(true) ; rb2.setChecked(false) ;rb3.setChecked(false);rb4.setChecked(false)}
                NotificationHour.TWO.hour -> { rb1.setChecked(false) ; rb2.setChecked(true) ;rb3.setChecked(false);rb4.setChecked(false) }
                NotificationHour.FIVE.hour -> { rb1.setChecked(false) ; rb2.setChecked(false) ;rb3.setChecked(true);rb4.setChecked(false)}
                NotificationHour.DAY.hour -> { rb1.setChecked(false) ; rb2.setChecked(false) ;rb3.setChecked(false);rb4.setChecked(true)}
            }
        })
    }



    private fun startWorkManger(){
        val constraintsBuilder: Constraints.Builder = Constraints.Builder()
        constraintsBuilder.setRequiredNetworkType(NetworkType.CONNECTED)
        val constraints: Constraints = constraintsBuilder.build()

        val notificationRequest = PeriodicWorkRequestBuilder<NotificationWork>(selectRadioButtonHour.toLong(), TimeUnit.HOURS) //TODO: Change with SelectedRadiobuton
            .addTag(WORK_MANAGER_TAG)
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniquePeriodicWork(WORK_MANAGER_TAG, ExistingPeriodicWorkPolicy.REPLACE, notificationRequest)
    }

    fun cancelWork() {
        workManager.cancelAllWorkByTag(WORK_MANAGER_TAG)
    }

}