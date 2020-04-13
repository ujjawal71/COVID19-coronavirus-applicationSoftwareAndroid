package com.csecoder.covid19.data.database.datafromoutside.updatetable

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.csecoder.covid19.data.database.model.updatetable.S1CoronaEntity
import com.csecoder.covid19.data.database.model.updatetable.S1CountryCoordEntity

@Dao
interface firstCoronaoutside {

    @Query("SELECT * FROM coronaentity")
    fun getAllCoronaDataS1(): S1CoronaEntity

    @Query("SELECT entry FROM coronaentity")
    fun getJsonEntryS1(): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCoronaDataS1(t: S1CoronaEntity)



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCountryCoordS1(t: S1CountryCoordEntity)

    @Query("SELECT * FROM countrycoord")
    fun getCountryCoordS1(): List<S1CountryCoordEntity>

    @Query("DELETE FROM countrycoord")
    suspend fun deleteAllCountryCoordS1()

}