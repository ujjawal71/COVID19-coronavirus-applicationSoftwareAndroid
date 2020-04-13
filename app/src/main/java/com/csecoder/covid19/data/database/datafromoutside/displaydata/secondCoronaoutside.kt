package com.csecoder.covid19.data.database.datafromoutside.displaydata

import androidx.room.Dao
import androidx.room.Query
import com.csecoder.covid19.data.database.datafromoutside.Baseoutside
import com.csecoder.covid19.data.database.model.displaydata.S2CoronaEntity


@Dao
interface secondCoronaoutside : Baseoutside<S2CoronaEntity> {

    @Query("SELECT * FROM corona_s2_entity")
    fun getAllCases(): List<S2CoronaEntity>

    @Query("SELECT SUM(stats_confirmed) FROM corona_s2_entity")
    fun getTotalConfirmedCase(): Int

    @Query("SELECT SUM(stats_deaths) FROM corona_s2_entity")
    fun getTotalDeathCase(): Int

    @Query("SELECT SUM(stats_recovered) FROM corona_s2_entity")
    fun getTotalRecoveredCase(): Int

    @Query("DELETE FROM corona_s2_entity")
    fun deleteAll()
}