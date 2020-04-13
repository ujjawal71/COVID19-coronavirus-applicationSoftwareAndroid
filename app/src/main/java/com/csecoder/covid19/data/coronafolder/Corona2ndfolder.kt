package com.csecoder.covid19.data.coronafolder

import androidx.lifecycle.MutableLiveData

import com.csecoder.covid19.UjjuBoard.Constants
import com.csecoder.covid19.UjjuBoard.InternetChecker
import com.csecoder.covid19.data.database.datafromoutside.datafromwebsite.thirdCoronaoutside
import com.csecoder.covid19.data.database.model.CoronaEntity
import com.csecoder.covid19.data.database.model.datafromwebsite.S3CoronaEntity
import com.csecoder.covid19.data.network.category.CoronaS3Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class Corona2ndfolder(
    private val thirdCoronaoutside: thirdCoronaoutside,
    private val coronaS3Service: CoronaS3Api
) {

    var coronaLiveDataS3: MutableLiveData<List<CoronaEntity>> = MutableLiveData()

    var isFinished: MutableLiveData<Map<String, Boolean>> = MutableLiveData()

    var totalCases: MutableLiveData<Map<String, String>> = MutableLiveData()

    fun getCoronaDataS3(): List<S3CoronaEntity> = thirdCoronaoutside.getAllCases()

    suspend fun callCoronaS3Data() = coronaS3Service.fetchArcGIS()

    fun fetchCoronaDataS3(coroutineContext: CoroutineContext = Dispatchers.IO) {

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

                        val caseResponse = callCoronaS3Data()

                        if (caseResponse.isSuccessful) {
                            thirdCoronaoutside.deleteAll()

                            caseResponse.body()?.let { it ->
                                coronaLiveDataS3.postValue(coronaEntityData(it.features))
                                thirdCoronaoutside.save(it.features)
                            }

                        }

                        val totalCasesMap: MutableMap<String, String> = mutableMapOf()


                        val confirmCaseResponse = coronaS3Service.fetchArcGISConfirmed()
                        if (confirmCaseResponse.isSuccessful) {
                            confirmCaseResponse.body()?.let {
                                totalCasesMap["confirmed"] =
                                    it.features[0].attributes.value.toString()
                            }
                        }
                        val deathCaseResponse = coronaS3Service.fetchArcGISDeaths()
                        if (deathCaseResponse.isSuccessful) {
                            deathCaseResponse.body()?.let {
                                totalCasesMap["deaths"] = it.features[0].attributes.value.toString()
                            }
                        }
                        val recoveredCaseResponse = coronaS3Service.fetchArcGISRecovered()
                        if (recoveredCaseResponse.isSuccessful) {
                            recoveredCaseResponse.body()?.let {
                                totalCasesMap["recovered"] =
                                    it.features[0].attributes.value.toString()
                            }
                        }

                        totalCases.postValue(
                            totalCasesMap
                        )


                    } else {

                        coronaLiveDataS3.postValue(coronaEntityData(thirdCoronaoutside.getAllCases()))

                        totalCases.postValue(
                            mapOf(
                                "confirmed" to thirdCoronaoutside.getTotalConfirmedCase().toString(),
                                "recovered" to thirdCoronaoutside.getTotalRecoveredCase().toString(),
                                "deaths" to thirdCoronaoutside.getTotalDeathCase().toString()
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

    fun coronaEntityData(data: List<S3CoronaEntity>): List<CoronaEntity> {

        val coronaEntity: MutableList<CoronaEntity> = mutableListOf()

        data.forEach {

            try {
                coronaEntity.add(
                    CoronaEntity(
                        data_source = Constants.DATA_SOURCE.DATA_S3.value,
                        data_source_name = "arcgis",
                        info = CoronaEntity.DataInfo(
                            country = it.attributes.attr_combinedKey,
                            latitude = it.attributes.attr_lat ?: 0.0,
                            longitude = it.attributes.attr_long ?: 0.0,
                            case_actives = 0,
                            case_confirms = it.attributes.attr_confirmed?.toLong(),
                            case_deaths = it.attributes.attr_deaths?.toLong(),
                            case_recovered = it.attributes.attr_recovered?.toLong(),
                            flags = ""
                        )
                    )
                )
            } catch (nfe: NumberFormatException) {
                coronaEntity.add(
                    CoronaEntity(
                        data_source = Constants.DATA_SOURCE.DATA_S3.value,
                        data_source_name = "arcgis",
                        info = CoronaEntity.DataInfo(
                            country = it.attributes.attr_combinedKey,
                            latitude = 0.0,
                            longitude = 0.0,
                            case_actives = 0,
                            case_confirms = it.attributes.attr_confirmed?.toLong(),
                            case_deaths = it.attributes.attr_deaths?.toLong(),
                            case_recovered = it.attributes.attr_recovered?.toLong(),
                            flags = ""
                        )
                    )
                )
            }

        }

        isFinished.postValue(
            mapOf(
                "done" to true,
                "internet" to true
            )
        )

        return coronaEntity
    }
}

