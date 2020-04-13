package com.csecoder.covid19.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class otherEntity(
    @PrimaryKey
    val id: Int,
    val value: String
)


