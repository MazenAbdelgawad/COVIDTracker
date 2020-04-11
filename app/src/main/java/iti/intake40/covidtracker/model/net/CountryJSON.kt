package iti.intake40.covidtracker.model.net

import com.google.gson.annotations.SerializedName
import iti.intake40.covidtracker.model.Country

data class CountryJSON(
    @SerializedName("countries_stat")
    val countriesStat: List<Country>,

    @SerializedName("statistic_taken_at")
    val statisticTakenAt: String
)