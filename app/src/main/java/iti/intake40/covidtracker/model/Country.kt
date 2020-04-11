package iti.intake40.covidtracker.model

import com.google.gson.annotations.SerializedName
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//@Serializable
@Entity(tableName = "country_table")
data class Country (
    @SerializedName("country_name")
    @PrimaryKey
    @ColumnInfo(name = "country_name")
    val countryName: String,

    val cases: String,
    val deaths: String,
    val region: String,

    @SerializedName("total_recovered")
    @ColumnInfo(name = "total_recovered")
    val totalRecovered: String,

    @SerializedName("new_deaths")
    @ColumnInfo(name = "new_deaths")
    val newDeaths: String,

    //@SerialName("new_cases")
    @ColumnInfo(name = "new_cases")
    val newCases: String,

    @SerializedName("serious_critical")
    @ColumnInfo(name = "serious_critical")
    val seriousCritical: String,

    @SerializedName("active_cases")
    @ColumnInfo(name = "active_cases")
    val activeCases: String,

    @SerializedName("total_cases_per_1m_population")
    @ColumnInfo(name = "total_cases_per_1m_population")
    val totalCasesPer1MPopulation: String
)
