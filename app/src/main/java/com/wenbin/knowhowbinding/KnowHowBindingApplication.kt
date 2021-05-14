package com.wenbin.knowhowbinding

import android.app.Application
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.util.ServiceLocator
import kotlin.properties.Delegates

class KnowHowBindingApplication : Application() {

    val knowHowBindingRepository : KnowHowBindingRepository
        get() = ServiceLocator.provideTasksRepository(this)

    companion object {
        var instance: KnowHowBindingApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}