package com.csecoder.covid19.data.database.datafromoutside

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.csecoder.covid19.data.database.model.otherEntity

@Dao
interface otheroutside : Baseoutside<otherEntity> {

    @Query("SELECT * FROM otherEntity")
    fun getAll(): LiveData<List<otherEntity>>

}