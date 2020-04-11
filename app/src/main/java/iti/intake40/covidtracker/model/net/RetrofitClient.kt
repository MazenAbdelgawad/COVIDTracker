package iti.intake40.covidtracker.model.net

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val BASE_URL = "https://coronavirus-monitor.p.rapidapi.com/coronavirus/"

    private val httpClient = OkHttpClient.Builder().addInterceptor(Interceptor {
        var request = it.request().newBuilder()
            .addHeader("x-rapidapi-host","coronavirus-monitor.p.rapidapi.com")
            .addHeader("x-rapidapi-key","9b0c12ecd3mshcde8149855df2f4p1408aajsn39cc2abd6f9d")
            .build()
        it.proceed(request)
    })


    fun makeRetrofitService(): RetrofitApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
            .create(RetrofitApi::class.java)
    }


}