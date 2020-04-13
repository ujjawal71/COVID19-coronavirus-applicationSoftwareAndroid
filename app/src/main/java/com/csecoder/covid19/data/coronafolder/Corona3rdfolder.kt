package com.csecoder.covid19.data.coronafolder

import androidx.lifecycle.MutableLiveData
import com.csecoder.covid19.data.database.datafromoutside.worldometerswebsite.forthCoronaoutside
import com.csecoder.covid19.data.database.model.worldometers.S4CoronaEntity


import com.csecoder.covid19.UjjuBoard.Constants
import com.csecoder.covid19.UjjuBoard.InternetChecker
import com.csecoder.covid19.data.database.model.CoronaEntity
import com.csecoder.covid19.data.network.category.CoronaS2Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


class Corona3rdfolder(
    private val forthCoronaoutside: forthCoronaoutside,
    private val coronaS4Service: CoronaS2Api
) {

    var coronaLiveDataS4: MutableLiveData<List<CoronaEntity>> = MutableLiveData()

    var isFinished: MutableLiveData<Map<String, Boolean>> = MutableLiveData()

    var totalCases: MutableLiveData<Map<String, String>> = MutableLiveData()

    fun getCoronaDataS4(): List<S4CoronaEntity> = forthCoronaoutside.getAllCases()

    suspend fun callCoronaS4Data() = coronaS4Service.fetchWorlOdMeters()

    fun fetchCoronaDataS4(coroutineContext: CoroutineContext = Dispatchers.IO) {

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

                        val caseResponse = callCoronaS4Data()

                        if (caseResponse.isSuccessful) {
                            forthCoronaoutside.deleteAll()

                            caseResponse.body()?.let { it ->
                                buildData(it)
                                forthCoronaoutside.save(it)
                            }

                        }

                        totalCases.postValue(
                            mapOf(
                                "confirmed" to forthCoronaoutside.getTotalConfirmedCase().toString(),
                                "recovered" to forthCoronaoutside.getTotalRecoveredCase().toString(),
                                "deaths" to forthCoronaoutside.getTotalDeathCase().toString()
                            )
                        )


                    } else {

                        buildData(forthCoronaoutside.getAllCases())

                        totalCases.postValue(
                            mapOf(
                                "confirmed" to forthCoronaoutside.getTotalConfirmedCase().toString(),
                                "recovered" to forthCoronaoutside.getTotalRecoveredCase().toString(),
                                "deaths" to forthCoronaoutside.getTotalDeathCase().toString()
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

    private suspend fun buildData(data: List<S4CoronaEntity>) {
        val coronaEntity: MutableList<CoronaEntity> = mutableListOf()

        // limits the scope of concurrency
        withContext(Dispatchers.IO) {

            data.forEach {

                withContext(Dispatchers.IO) {
                    try {
                        coronaEntity.add(
                            CoronaEntity(
                                data_source = Constants.DATA_SOURCE.DATA_S4.value,
                                data_source_name = "worldometers",
                                info = CoronaEntity.DataInfo(
                                    country = it.country,
                                    latitude = it.countryInfo.info_lat ?: 0.0,
                                    longitude = it.countryInfo.info_long ?: 0.0,
                                    case_actives = it.active,
                                    case_confirms = it.cases,
                                    case_deaths = it.deaths,
                                    case_recovered = it.recovered,
                                    flags = it.countryInfo.info_flag
                                )
                            )
                        )
                    } catch (nfe: NumberFormatException) {
                        coronaEntity.add(
                            CoronaEntity(
                                data_source = Constants.DATA_SOURCE.DATA_S4.value,
                                data_source_name = "worldometers",
                                info = CoronaEntity.DataInfo(
                                    country = it.country,
                                    latitude = 0.0,
                                    longitude = 0.0,
                                    case_actives = it.active,
                                    case_confirms = it.cases,
                                    case_deaths = it.deaths,
                                    case_recovered = it.recovered,
                                    flags = it.countryInfo.info_flag
                                )
                            )
                        )
                    }
                }

            }

            isFinished.postValue(
                mapOf(
                    "done" to true,
                    "internet" to true
                )
            )
            coronaLiveDataS4.postValue(coronaEntity)
        }

    }

}

