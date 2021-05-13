package com.wenbin.knowhowbinding

import android.app.Application
import com.wenbin.knowhowbinding.data.KnowHowBindingRepository
import com.wenbin.knowhowbinding.util.ServiceLocator
import kotlin.properties.Delegates

class KnowHowBindingApplication : Application() {

    val knowHowBindingRepository : KnowHowBindingRepository
        get() = ServiceLocator.provideTasksRepository(this)

    companion object {
        var INSTANCE: KnowHowBindingApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}