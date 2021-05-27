package com.wenbin.knowhowbinding

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.work.*
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.util.KEY_EVENT_CONTENT
import com.wenbin.knowhowbinding.util.KEY_EVENT_TIME
import com.wenbin.knowhowbinding.util.ServiceLocator
import com.wenbin.knowhowbinding.worker.MyWorker
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class KnowHowBindingApplication : Application() {

    val repository : KnowHowBindingRepository
        get() = ServiceLocator.provideTasksRepository(this)

    companion object {
        var instance: KnowHowBindingApplication by Delegates.notNull()
        lateinit var appContext : Context

    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        instance = this
    }

    fun isLiveDataDesign() = true

    fun setWork (eventTime: Long, eventContent: String) {

        // Set firing time 6 hours early
        val diffTime = eventTime - 21600000 - Calendar.getInstance().timeInMillis

        // Create the Constraints
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Define the input
        val eventData = workDataOf(KEY_EVENT_TIME to eventTime, KEY_EVENT_CONTENT to eventContent)

        // Bring it all together by creating the WorkRequest; this also sets the back off criteria
        val uploadWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
            .setInputData(eventData)
            .setConstraints(constraints)
            .setInitialDelay(diffTime, TimeUnit.MILLISECONDS)
            .build()

        // Set work
        WorkManager.getInstance(this).enqueue(uploadWorkRequest)

    }
}