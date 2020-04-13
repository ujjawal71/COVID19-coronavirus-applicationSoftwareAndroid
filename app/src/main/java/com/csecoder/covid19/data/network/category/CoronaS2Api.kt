package com.csecoder.covid19.data.network.category

import com.csecoder.covid19.data.database.model.displaydata.S2CoronaEntity
import com.csecoder.covid19.data.database.model.worldometers.S4CoronaEntity
import retrofit2.Response
import retrofit2.http.GET


interface CoronaS2Api {

    @GET("v2/jhucsse")
    suspend fun fetchJHUCSSE(): Response<List<S2CoronaEntity>>

    @GET("countries")
    suspend fun fetchWorlOdMeters(): Response<List<S4CoronaEntity>>

}


