package iti.intake40.covidtracker.model.net

import iti.intake40.covidtracker.model.Country
import retrofit2.Response
import retrofit2.http.GET


interface RetrofitApi {
    @GET("cases_by_country.php")
    suspend fun loadCountries(): Response<CountryJSON>
}