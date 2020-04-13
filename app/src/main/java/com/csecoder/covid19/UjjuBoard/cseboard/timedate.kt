package com.csecoder.covid19.UjjuBoard.cseboard

import java.text.SimpleDateFormat
import java.util.*


fun Date.toSimpleString() : String {
    val format = SimpleDateFormat("dd MMM yyy, EEEE hh:mm:ss a", Locale.ENGLISH)
    return format.format(this)
}