package com.wenbin.knowhowbinding.ext

import com.wenbin.knowhowbinding.data.Event

fun List<Event>?.sortByTimeStamp (selectedTime: Long) : List<Event> {
    return this?.filter {
        it?.let {
            selectedTime <= it.eventTime && it.eventTime < selectedTime + 86400000
        }
    }
        ?: listOf()
}