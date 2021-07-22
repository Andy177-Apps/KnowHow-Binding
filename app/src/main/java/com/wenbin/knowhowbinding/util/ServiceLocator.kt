package com.wenbin.knowhowbinding.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.wenbin.knowhowbinding.data.source.DefaultKnowHowBindingRepository
import com.wenbin.knowhowbinding.data.source.KnowHowBindingDataSource
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.data.source.local.KnowHowBindingLocalDataSource
import com.wenbin.knowhowbinding.data.source.remote.KnowHowBindingRemoteDataSource

object ServiceLocator {
    @Volatile
    var knowHowBindingRepository: KnowHowBindingRepository? = null
        @VisibleForTesting set

    fun provideTasksRepository(context: Context): KnowHowBindingRepository {
        synchronized(this) {
            return knowHowBindingRepository
                ?: knowHowBindingRepository
                ?: createKnowHowBindingRepository(context)
        }
    }

    private fun createKnowHowBindingRepository(context: Context): KnowHowBindingRepository {
        return DefaultKnowHowBindingRepository(
            KnowHowBindingRemoteDataSource,
            createLocalDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): KnowHowBindingDataSource {
        return KnowHowBindingLocalDataSource(context)
    }
}