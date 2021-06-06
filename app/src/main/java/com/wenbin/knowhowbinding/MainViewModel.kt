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

    // isAsked user to edit information
    // The reason for putting variables in Main ViewModel instead of Home ViewModel is to notify the user only once.
    val noticed = MutableLiveData<Boolean>(false)
}