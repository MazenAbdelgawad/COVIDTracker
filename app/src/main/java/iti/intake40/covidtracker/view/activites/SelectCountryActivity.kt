package iti.intake40.covidtracker.view.activites

import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock.sleep
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import iti.intake40.covidtracker.R
import iti.intake40.covidtracker.model.Const
import iti.intake40.covidtracker.model.Country
import iti.intake40.covidtracker.model.NotificationHour
import iti.intake40.covidtracker.view.adapters.SelectCountryAdapter
import iti.intake40.covidtracker.viewmodel.SelectCountryViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_select_country.*
import kotlinx.android.synthetic.main.activity_select_country.day_id
import kotlinx.android.synthetic.main.activity_select_country.month_id
import kotlinx.android.synthetic.main.activity_select_country.txt_country_name_selected
import kotlinx.android.synthetic.main.activity_select_country.year_id


class SelectCountryActivity : AppCompatActivity() {

    private var selectCountryViewModel: SelectCountryViewModel? = null
    var selectRadioButtonHour : Int = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_country)

        selectCountryViewModel = ViewModelProviders.of(this).get(SelectCountryViewModel::class.java)
        selectCountryViewModel?.getAllCountry()?.observe(this, Observer<List<Country>> {
            setupView(it)
        })

        val sharedPref: SharedPreferences = getSharedPreferences(Const.PREF_NAME, 0)
        txt_country_name_selected.text = sharedPref.getString(Const.PREF_NAME , "")

        var houre = sharedPref.getInt(Const.PREF_HORE,2)

         when(houre) {
             NotificationHour.ONE.hour -> { rb1.setChecked(true) ; rb2.setChecked(false) ;rb3.setChecked(false);rb4.setChecked(false)}
             NotificationHour.TWO.hour -> { rb1.setChecked(false) ; rb2.setChecked(true) ;rb3.setChecked(false);rb4.setChecked(false) }
             NotificationHour.FIVE.hour -> { rb1.setChecked(false) ; rb2.setChecked(false) ;rb3.setChecked(true);rb4.setChecked(false)}
             NotificationHour.DAY.hour -> { rb1.setChecked(false) ; rb2.setChecked(false) ;rb3.setChecked(false);rb4.setChecked(true)}
        }

        setTimeing()

    }




    fun setupView(list: List<Country>) {
        val layout = LinearLayoutManager(applicationContext)
        recycler_select_country.layoutManager = layout
        recycler_select_country.adapter = SelectCountryAdapter(list) {
              txt_country_name_selected.text =  it
        }
    }

    fun onRadioButtonClicked(view: View) {

        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.rb1 ->
                    if (checked) {
                        // Pirates are the best
                        print("chked 1")
                        rb2.setChecked(false)
                        rb3.setChecked(false)
                        rb4.setChecked(false)
                        selectRadioButtonHour = 1

                    }

                R.id.rb2 ->
                    if (checked) {
                        // Ninjas rule
                        rb1.setChecked(false)
                        rb3.setChecked(false)
                        rb4.setChecked(false)
                        selectRadioButtonHour = 2
                    }

                R.id.rb3 ->
                    if (checked) {
                        // Pirates are the best
                        rb2.setChecked(false)
                        rb1.setChecked(false)
                        rb4.setChecked(false)
                        selectRadioButtonHour = 5
                    }

                R.id.rb4 ->
                    if (checked) {
                        // Ninjas rule
                        rb2.setChecked(false)
                        rb3.setChecked(false)
                        rb1.setChecked(false)
                        selectRadioButtonHour = 24
                    }
            }


        }
    }

    fun clickBtnSave(view: View) {

        if(txt_country_name_selected.text.isNotEmpty()){
            val sharedPref: SharedPreferences = getSharedPreferences(Const.PREF_NAME, 0)
            sharedPref.edit().putString(Const.PREF_NAME,txt_country_name_selected.text.toString()).commit()
            sharedPref.edit().putInt(Const.PREF_HORE ,selectRadioButtonHour).commit()
            finish()
        }else{
            Toast.makeText(this, "Please Select country", Toast.LENGTH_LONG).show()
        }
    }

    fun setTimeing (){
        val sharedPref: SharedPreferences = getSharedPreferences(Const.PREF_NAME, 0)
        year_id.text = sharedPref.getString(Const.PREF_YEAR,"")
        month_id.text  = sharedPref.getString(Const.PREF_MONTH , "")
        day_id.text = sharedPref.getString(Const.PREF_DAY , "")
    }

}