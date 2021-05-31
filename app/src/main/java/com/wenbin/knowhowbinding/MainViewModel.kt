package com.wenbin.knowhowbinding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository
import com.wenbin.knowhowbinding.util.CurrentFragmentType

class MainViewModel(private val repository: KnowHowBindingRepository) : ViewModel() {

    // Record current fragment to support data binding
    val currentFragmentType = MutableLiveData<CurrentFragmentType>()

    // Save button in edit page is pressed.
    val saveIsPressed = MutableLiveData<Boolean>()

}