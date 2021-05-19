package com.wenbin.knowhowbinding

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.util.ServiceLocator
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
}