package com.csecoder.covid19.patient

import org.koin.dsl.module
import java.util.*

val appModule = module {
    single { provideCalendar() }
}

private fun provideCalendar() = Calendar.getInstance()
