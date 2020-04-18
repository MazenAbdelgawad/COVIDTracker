package iti.intake40.covidtracker.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iti.intake40.covidtracker.R
import iti.intake40.covidtracker.model.Country
import kotlinx.android.synthetic.main.card_country.view.*

class HomeAdapter (private val list: List<Country>): RecyclerView.Adapter<HomeAdapter.VH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.card_country,parent,false)
        return VH(inflate)  //as VH
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.txt_card_select_county.text = list[position].countryName
        holder.itemView.card_total_cases.text = list[position].cases
        holder.itemView.card_total_coverd.text = list[position].totalRecovered
        holder.itemView.card_total_deaths.text = list[position].deaths
    }


    class VH(itemView: View) : RecyclerView.ViewHolder(itemView)
}