package com.csecoder.covid19.UjjuBoard

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.csecoder.covid19.R

import com.csecoder.covid19.data.database.model.datafromwebsite.S3CoronaEntity
import com.csecoder.covid19.data.database.model.displaydata.S2CoronaEntity
import com.csecoder.covid19.data.database.model.worldometers.S4CoronaEntity
import com.csecoder.covid19.data.coronafolder.Coronafirstfolder

import com.csecoder.covid19.data.coronafolder.Corona2ndfolder
import com.csecoder.covid19.data.coronafolder.Corona3rdfolder

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.coroutines.CoroutineContext


class NotifyWorker(
    context: Context, workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val coronaS1Repository: Coronafirstfolder by inject()
    private val coronaS2Repository: Coronafirstfolder by inject()
    private val corona2ndfolder: Corona2ndfolder by inject()
    private val Corona3rdfolder: Corona3rdfolder by inject()
    private val preferences: SharedPreferences by inject()

    private val mContext = context

    private var oldDataCountCase = 0
    private var newDataCountCase = 0

    override suspend fun doWork(): Result {

        fetchcoronaData()

        return Result.success()
    }


    private fun fetchcoronaData(coroutineContext: CoroutineContext = Dispatchers.IO) {

        CoroutineScope(coroutineContext).launch {

            when (preferences.getString(
                Constants.PREF_DATA_SOURCE,
                Constants.DATA_SOURCE.DATA_S4.value
            )) {
                Constants.DATA_SOURCE.DATA_S1.value -> {

                }

                Constants.DATA_SOURCE.DATA_S2.value -> {


                    val s2CoronaOldEntry: List<S2CoronaEntity> =
                        coronaS2Repository.getCoronaDataS2()
                    var s2CoronaNewEntry: List<S2CoronaEntity> = listOf()

                    coronaS2Repository.callCoronaDataS2().let {
                        if(it.isSuccessful){
                            s2CoronaNewEntry = it.body()!!
                        }
                    }

                    s2CoronaOldEntry.forEach { value ->

                        oldDataCountCase += try {
                            value.stats.confirmed!!.toInt()
                        } catch (nfe: NumberFormatException) {
                            1
                        }

                    }

                    s2CoronaNewEntry.forEach { value ->

                        newDataCountCase += try {
                            value.stats.confirmed!!.toInt()
                        } catch (nfe: NumberFormatException) {
                            1
                        }

                    }
                }

                Constants.DATA_SOURCE.DATA_S3.value -> {


                    val s3CoronaOldEntry: List<S3CoronaEntity> =
                        corona2ndfolder.getCoronaDataS3()
                    var s3CoronaNewEntry: List<S3CoronaEntity> = listOf()

                    corona2ndfolder.callCoronaS3Data().let {
                        if(it.isSuccessful){
                            s3CoronaNewEntry = it.body()!!.features
                        }
                    }

                    s3CoronaOldEntry.forEach { value ->

                        oldDataCountCase += try {
                            value.attributes.attr_confirmed!!.toInt()
                        } catch (nfe: NumberFormatException) {
                            1
                        }

                    }

                    s3CoronaNewEntry.forEach { value ->

                        newDataCountCase += try {
                            value.attributes.attr_confirmed!!.toInt()
                        } catch (nfe: NumberFormatException) {
                            1
                        }

                    }
                }

                Constants.DATA_SOURCE.DATA_S4.value -> {


                    val s4CoronaOldEntry: List<S4CoronaEntity> =
                        Corona3rdfolder.getCoronaDataS4()
                    var s4CoronaNewEntry: List<S4CoronaEntity> = listOf()

                    Corona3rdfolder.callCoronaS4Data().let {
                        if(it.isSuccessful){
                            s4CoronaNewEntry = it.body()!!
                        }
                    }

                    s4CoronaOldEntry.forEach { value ->

                        oldDataCountCase += try {
                            value.cases!!.toInt()
                        } catch (nfe: NumberFormatException) {
                            1
                        }

                    }

                    s4CoronaNewEntry.forEach { value ->

                        newDataCountCase += try {
                            value.cases!!.toInt()
                        } catch (nfe: NumberFormatException) {
                            1
                        }

                    }
                }

            }



            if (oldDataCountCase != newDataCountCase)
                sendNotification(newDataCountCase - oldDataCountCase)

        }
    }

    private fun sendNotification(total: Int) {
        val notificationManager =
            mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "WorkManager_01"

        //If on Oreo then notification required a notification channel.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "WorkManager",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(mContext, channelId)
            .setContentTitle("Covid19 Update!")
            .setContentText(
                "$total new cases has been confirmed"
            )
            .setSmallIcon(R.mipmap.ic_launcher)

        notificationManager.notify(2, notification.build())
    }
}

