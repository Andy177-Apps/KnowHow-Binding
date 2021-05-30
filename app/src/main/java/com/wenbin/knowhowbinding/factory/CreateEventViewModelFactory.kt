package com.wenbin.knowhowbinding.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wenbin.knowhowbinding.calendar.createevent.CreateEventViewModel
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository

@Suppress("UNCHECKED_CAST")
class CreateEventViewModelFactory(
    private val repository: KnowHowBindingRepository,
    private val selectedDate: Long
) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(CreateEventViewModel::class.java)) {
            return CreateEventViewModel(repository, selectedDate) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
