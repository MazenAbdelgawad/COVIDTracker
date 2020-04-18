package iti.intake40.covidtracker.model.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import iti.intake40.covidtracker.model.Country

@Dao
interface CountryDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setCountries(countries: List<Country>)

    @Query("SELECT * FROM country_table") //ORDER BY id ASC")
    fun getCountries(): LiveData<List<Country>>

    @Query("DELETE FROM country_table")
    fun deleteAllCountries()

    @Query("SELECT * FROM country_table WHERE country_name = :country_name")
    fun getCountry(country_name: String): LiveData<Country>
}