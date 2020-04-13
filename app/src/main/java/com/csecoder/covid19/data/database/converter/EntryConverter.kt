package com.csecoder.covid19.data.database.converter

import androidx.room.TypeConverter
import com.csecoder.covid19.data.database.model.updatetable.S1CoronaEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class EntryConverter {
    companion object {
        private val gson = Gson()

        @TypeConverter
        @JvmStatic
        fun toEntry(data: String?): List<S1CoronaEntity.Entry> {
            if (data == null) {
                return emptyList()
            }
            val listType = object : TypeToken<List<S1CoronaEntity.Entry>>() {}.type
            return gson.fromJson(data, listType)
        }

        @TypeConverter
        @JvmStatic
        fun fromEntry(entry: List<S1CoronaEntity.Entry>): String {
            return gson.toJson(entry)
        }
    }
}