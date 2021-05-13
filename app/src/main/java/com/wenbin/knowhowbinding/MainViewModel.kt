package com.wenbin.knowhowbinding

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.data.KnowHowBindingRepository
import com.wenbin.knowhowbinding.util.CurrentFragmentType

class MainViewModel(private val knowHowBindingRepository: KnowHowBindingRepository) : ViewModel() {

    // Record current fragment to support data binding
    val currentFragmentType = MutableLiveData<CurrentFragmentType>()

    fun runLog() {
        Log.d("wenbin", "shfoisfidsjfisfofsisfdo")
    }
}