package com.csecoder.covid19.data.coronafolder

import androidx.lifecycle.MutableLiveData

import com.csecoder.covid19.UjjuBoard.Constants
import com.csecoder.covid19.UjjuBoard.InternetChecker
import com.csecoder.covid19.data.database.datafromoutside.displaydata.secondCoronaoutside
import com.csecoder.covid19.data.database.model.CoronaEntity
import com.csecoder.covid19.data.database.model.displaydata.S2CoronaEntity
import com.csecoder.covid19.data.network.category.CoronaS2Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class Coronafirstfolder(
    private val secondCoronaoutside: secondCoronaoutside,
    private val coronaS2Service: CoronaS2Api
) {

    var coronaLiveDataS2: MutableLiveData<List<CoronaEntity>> = MutableLiveData()

    var isFinished: MutableLiveData<Map<String, Boolean>> = MutableLiveData()

    var totalCases: MutableLiveData<Map<String, String>> = MutableLiveData()

    fun getCoronaDataS2(): List<S2CoronaEntity> = secondCoronaoutside.getAllCases()

    suspend fun callCoronaDataS2() = coronaS2Service.fetchJHUCSSE()

    fun fetchCoronaDataS2(coroutineContext: CoroutineContext = Dispatchers.IO) {

        isFinished.postValue(
            mapOf(
                "done" to false,
                "internet" to false
            )
        )

        InternetChecker(object : InternetChecker.Consumer {
            override fun accept(internet: Boolean) {

                CoroutineScope(coroutineContext).launch {

                    if (internet) {

                        val response = callCoronaDataS2()

                        if (response.isSuccessful) {
                            secondCoronaoutside.deleteAll()

                            response.body()?.let { it ->
                                coronaLiveDataS2.postValue(coronaEntityData(it))
                                secondCoronaoutside.save(it)
                            }

                            totalCases.postValue(
                                mapOf(
                                    "confirmed" to secondCoronaoutside.getTotalConfirmedCase().toString(),
                                    "recovered" to secondCoronaoutside.getTotalRecoveredCase().toString(),
                                    "deaths" to secondCoronaoutside.getTotalDeathCase().toString()
                                )
                            )

                        }

                    } else {


                        coronaLiveDataS2.postValue(coronaEntityData(secondCoronaoutside.getAllCases()))

                        totalCases.postValue(
                            mapOf(
                                "confirmed" to secondCoronaoutside.getTotalConfirmedCase().toString(),
                                "recovered" to secondCoronaoutside.getTotalRecoveredCase().toString(),
                                "deaths" to secondCoronaoutside.getTotalDeathCase().toString()
                            )
                        )

                        isFinished.postValue(
                            mapOf(
                                "done" to true,
                                "internet" to false
                            )
                        )
                    }
                }
            }
        })
    }

    fun coronaEntityData(data: List<S2CoronaEntity>): List<CoronaEntity> {

        val coronaEntity: MutableList<CoronaEntity> = mutableListOf()

        data.forEach {

            try {
                coronaEntity.add(
                    CoronaEntity(
                        data_source = Constants.DATA_SOURCE.DATA_S2.value,
                        data_source_name = "John Hopkins CSSE",
                        info = CoronaEntity.DataInfo(
                            country = "${it.country} ${

                            if(!it.province.isNullOrEmpty()) {
                                "("+it.province+")"
                            } else {
                                ""
                            }
                            }",
                            latitude = it.coordinates.latitude?.toDouble(),
                            longitude = it.coordinates.longitude?.toDouble(),
                            case_actives = 0,
                            case_confirms = it.stats.confirmed?.toLong(),
                            case_deaths = it.stats.deaths?.toLong(),
                            case_recovered = it.stats.recovered?.toLong(),
                            flags = ""
                        )
                    )
                )
            } catch (nfe: NumberFormatException) {
                coronaEntity.add(
                    CoronaEntity(
                        data_source = Constants.DATA_SOURCE.DATA_S2.value,
                        data_source_name = "John Hopkins CSSE",
                        info = CoronaEntity.DataInfo(
                            country = "${it.country} ${

                            if(!it.province.isNullOrEmpty()) {
                                "("+it.province+")"
                            } else {
                                ""
                            }
                            }",
                            latitude = 0.0,
                            longitude = 0.0,
                            case_actives = 0,
                            case_confirms = it.stats.confirmed?.toLong(),
                            case_deaths = it.stats.deaths?.toLong(),
                            case_recovered = it.stats.recovered?.toLong(),
                            flags = ""
                        )
                    )
                )
            }

            isFinished.postValue(
                mapOf(
                    "done" to true,
                    "internet" to true
                )
            )
        }

        return coronaEntity
    }
}

