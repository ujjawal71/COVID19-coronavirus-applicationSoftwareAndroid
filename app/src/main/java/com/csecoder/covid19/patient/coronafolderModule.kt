package com.csecoder.covid19.patient

import com.csecoder.covid19.data.coronafolder.*

import org.koin.dsl.module

val repositoryModule = module {


    single {
        Coronafirstfolder(secondCoronaoutside = get(),coronaS2Service = get())
    }
    single {
        Corona2ndfolder(thirdCoronaoutside = get(),coronaS3Service = get())
    }
    single {
        Corona3rdfolder(forthCoronaoutside = get(),coronaS4Service = get())
    }
}