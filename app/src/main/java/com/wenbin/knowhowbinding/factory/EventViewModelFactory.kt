package com.wenbin.knowhowbinding.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wenbin.knowhowbinding.calendar.eventdetail.EventDetailViewModel
import com.wenbin.knowhowbinding.data.Event
import com.wenbin.knowhowbinding.data.source.KnowHowBindingRepository

@Suppress("UNCHECKED_CAST")
class EventViewModelFactory(
    private val repository: KnowHowBindingRepository,
    private val event: Event
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(EventDetailViewModel::class.java)) {
            return EventDetailViewModel(repository, event) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}