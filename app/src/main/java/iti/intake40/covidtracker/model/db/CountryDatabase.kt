package iti.intake40.covidtracker.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import iti.intake40.covidtracker.model.Country

@Database(entities = [Country::class], version = 1, exportSchema = false)
abstract class CountryDatabase : RoomDatabase() {

    abstract fun countryDao(): CountryDao

    companion object{
        @Volatile // volatile, meaning that writes to this field are immediately made visible to other threads.
        private var INSTANCE: CountryDatabase? = null

        fun getDatabase(context: Context): CountryDatabase?{
            if(INSTANCE == null){
                synchronized(CountryDatabase::class.java){
                    if(INSTANCE == null){
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                            CountryDatabase::class.java,
                            "country_database")
                            .build()
                    }
                }
            }
            return INSTANCE
        }

    }

}