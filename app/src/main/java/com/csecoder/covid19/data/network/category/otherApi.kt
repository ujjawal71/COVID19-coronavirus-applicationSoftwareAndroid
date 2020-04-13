package com.csecoder.covid19.data.network.category

import com.csecoder.covid19.data.database.model.otherEntity
import retrofit2.Response
import retrofit2.http.GET

interface otherApi {
    @GET("v2/5d08c0f2340000f79d5d9a31")
    suspend fun fetchAll(): Response<List<otherEntity>>
}