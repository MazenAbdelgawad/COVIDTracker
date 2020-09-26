package iti.intake40.covidtracker.model

import iti.intake40.covidtracker.model.db.CountryDatabase
import iti.intake40.covidtracker.model.net.createRetrofitService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single { CountryDatabase.getDatabase(get()) }
    single { get<CountryDatabase>().countryDao() }

    single { createRetrofitService() }

    single { androidContext().getSharedPreferences(Const.PREF_NAME ,0) }

    single { CountryRepository(get(),get(),get()) }
}