package com.csecoder.covid19.patient


import com.csecoder.covid19.secondscreencode.mainscreencode.maincode.MainActivityViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { MainActivityViewModel(get(), get(), get(), get()) }
}