package com.csecoder.covid19.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.csecoder.covid19.data.database.datafromoutside.worldometerswebsite.forthCoronaoutside

import com.csecoder.covid19.data.database.converter.EntryConverter
import com.csecoder.covid19.data.database.converter.ListStringTypeConverter
import com.csecoder.covid19.data.database.datafromoutside.otheroutside
import com.csecoder.covid19.data.database.datafromoutside.datafromwebsite.thirdCoronaoutside
import com.csecoder.covid19.data.database.datafromoutside.updatetable.firstCoronaoutside
import com.csecoder.covid19.data.database.datafromoutside.displaydata.secondCoronaoutside
import com.csecoder.covid19.data.database.model.otherEntity
import com.csecoder.covid19.data.database.model.datafromwebsite.S3CoronaEntity
import com.csecoder.covid19.data.database.model.updatetable.S1CoronaEntity
import com.csecoder.covid19.data.database.model.updatetable.S1CountryCoordEntity
import com.csecoder.covid19.data.database.model.displaydata.S2CoronaEntity
import com.csecoder.covid19.data.database.model.worldometers.S4CoronaEntity


@Database(
    entities = [otherEntity::class,
        S1CountryCoordEntity::class,
        S1CoronaEntity::class,
        S2CoronaEntity::class,
        S3CoronaEntity::class,
        S4CoronaEntity::class],
    version = 11,
    exportSchema = false
)
@TypeConverters(EntryConverter::class, ListStringTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dummyDao(): otheroutside
    abstract fun coronaDao1(): firstCoronaoutside
    abstract fun coronaDao2(): secondCoronaoutside
    abstract fun coronaDao3(): thirdCoronaoutside
    abstract fun coronaDao4(): forthCoronaoutside
}