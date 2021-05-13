package com.wenbin.knowhowbinding.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.wenbin.knowhowbinding.data.DefaultKnowHowBindingRepository
import com.wenbin.knowhowbinding.data.KnowHowBindingDataSource
import com.wenbin.knowhowbinding.data.KnowHowBindingRepository
import com.wenbin.knowhowbinding.data.local.KnowHowBindingLocalDataSource
import com.wenbin.knowhowbinding.data.remote.KnowHowBindingRemoteDataSource

object ServiceLocator {
    @Volatile
    var knowHowBindingRepository : KnowHowBindingRepository? = null
        @VisibleForTesting set

    fun provideTasksRepository(context: Context): KnowHowBindingRepository {
        synchronized(this) {
            return knowHowBindingRepository
                ?: knowHowBindingRepository
                ?: createStylishRepository(context)
        }
    }

    private fun createStylishRepository(context: Context): KnowHowBindingRepository {
        return DefaultKnowHowBindingRepository(KnowHowBindingRemoteDataSource,
            createLocalDataSource(context)
        )
    }

    private fun createLocalDataSource(context: Context): KnowHowBindingDataSource {
        return KnowHowBindingLocalDataSource(context)
    }
}