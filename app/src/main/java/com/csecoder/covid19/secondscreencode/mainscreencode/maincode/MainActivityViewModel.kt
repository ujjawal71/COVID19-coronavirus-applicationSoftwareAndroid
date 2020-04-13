package com.csecoder.covid19.secondscreencode.mainscreencode.maincode

import android.content.SharedPreferences
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csecoder.covid19.data.database.model.CoronaEntity
import com.csecoder.covid19.data.coronafolder.Coronafirstfolder
import com.csecoder.covid19.data.coronafolder.Corona2ndfolder
import com.csecoder.covid19.data.coronafolder.Corona3rdfolder
import com.csecoder.covid19.UjjuBoard.Constants


class MainActivityViewModel(
    private val coronaS2Repository: Coronafirstfolder,
    private val corona2ndfolder: Corona2ndfolder,
    private val Corona3rdfolder: Corona3rdfolder,
    private val preferences: SharedPreferences
) : ViewModel() {

    var isFinishedLiveData = MediatorLiveData<Map<String, Boolean>>()
    private var isFinishedSource: MutableLiveData<Map<String, Boolean>> = MutableLiveData()

    var coronaLiveData = MediatorLiveData<List<CoronaEntity>>()
    private var coronaLiveDataSource: MutableLiveData<List<CoronaEntity>> = MutableLiveData()

    var totalCases = MediatorLiveData<Map<String, String>>()
    private var totalCasesSource: MutableLiveData<Map<String, String>> = MutableLiveData()

    private var currentDataSource: MutableLiveData<String> = MutableLiveData()

    init {
        refreshData()
    }

    fun refreshData() {

        isFinishedLiveData.removeSource(isFinishedSource)
        coronaLiveData.removeSource(coronaLiveDataSource)
        totalCases.removeSource(totalCasesSource)

        when(preferences.getString(
            Constants.PREF_DATA_SOURCE,
            Constants.DATA_SOURCE.DATA_S4.value
        )){
            Constants.DATA_SOURCE.DATA_S1.value -> coronaDataSourceS1()
            Constants.DATA_SOURCE.DATA_S2.value -> coronaDataSourceS2()
            Constants.DATA_SOURCE.DATA_S3.value -> coronaDataSourceS3()
            Constants.DATA_SOURCE.DATA_S4.value -> coronaDataSourceS4()
        }
    }

    private fun coronaDataSourceS1() {
    }

    private fun coronaDataSourceS2() {
        currentDataSource.postValue(Constants.DATA_SOURCE.DATA_S2.value)

        coronaS2Repository.fetchCoronaDataS2()

        coronaLiveDataSource = coronaS2Repository.coronaLiveDataS2
        totalCasesSource = coronaS2Repository.totalCases
        isFinishedSource = coronaS2Repository.isFinished


        isFinishedLiveData.addSource(isFinishedSource) {
            isFinishedLiveData.value = it
        }
        totalCases.addSource(totalCasesSource) {
            totalCases.value = it
        }
        coronaLiveData.addSource(coronaLiveDataSource) {
            coronaLiveData.value = it
        }
    }

    private fun coronaDataSourceS3() {
        currentDataSource.postValue(Constants.DATA_SOURCE.DATA_S3.value)

        corona2ndfolder.fetchCoronaDataS3()

        coronaLiveDataSource = corona2ndfolder.coronaLiveDataS3
        totalCasesSource = corona2ndfolder.totalCases
        isFinishedSource = corona2ndfolder.isFinished


        isFinishedLiveData.addSource(isFinishedSource) {
            isFinishedLiveData.value = it
        }
        totalCases.addSource(totalCasesSource) {
            totalCases.value = it
        }
        coronaLiveData.addSource(coronaLiveDataSource) {
            coronaLiveData.value = it
        }
    }


    private fun coronaDataSourceS4() {
        currentDataSource.postValue(Constants.DATA_SOURCE.DATA_S4.value)

        Corona3rdfolder.fetchCoronaDataS4()

        coronaLiveDataSource = Corona3rdfolder.coronaLiveDataS4
        totalCasesSource = Corona3rdfolder.totalCases
        isFinishedSource = Corona3rdfolder.isFinished


        isFinishedLiveData.addSource(isFinishedSource) {
            isFinishedLiveData.value = it
        }
        totalCases.addSource(totalCasesSource) {
            totalCases.value = it
        }
        coronaLiveData.addSource(coronaLiveDataSource) {
            coronaLiveData.value = it
        }
    }

}