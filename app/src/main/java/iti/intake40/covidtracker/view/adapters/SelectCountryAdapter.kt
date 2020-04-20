package iti.intake40.covidtracker.view.adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import iti.intake40.covidtracker.R
import iti.intake40.covidtracker.model.Country
import iti.intake40.covidtracker.view.activites.SubscribeActivity
import kotlinx.android.synthetic.main.card_country.view.*

class SelectCountryAdapter (private val list: List<Country>, private val callbackSelected: (Country)->Unit): RecyclerView.Adapter<SelectCountryAdapter.VH>() {
     lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        context = parent.context
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.card_select_country,parent,false)
        return VH(inflate)  //as VH
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.txt_card_select_county.text = list[position].countryName
        holder.itemView.txt_card_select_county.setOnClickListener {
//            Toast.makeText(context,"dddddddddddd",Toast.LENGTH_LONG).show()
//            val sharedPref: SharedPreferences = context.getSharedPreferences(SubscribeActivity.PREF_NAME, 0)
//            sharedPref.edit().putString(SubscribeActivity.PREF_NAME,list[position].countryName).commit()
            callbackSelected(list[position])
        }
    }


    class VH(itemView: View) : RecyclerView.ViewHolder(itemView)
}