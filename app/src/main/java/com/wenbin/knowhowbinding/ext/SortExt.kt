package com.wenbin.knowhowbinding.ext

import com.wenbin.knowhowbinding.data.Answer
import com.wenbin.knowhowbinding.data.Event
import com.wenbin.knowhowbinding.data.User

fun List<Event>?.sortByTimeStamp (selectedTime: Long) : List<Event> {
    return this?.filter {
        it.let {
            selectedTime <= it.eventTime && it.eventTime < selectedTime + 86400000
        }
    }
        ?: listOf()
}

fun List<User>?.sortByTraits(answers : Answer) : List<User> {
    return this?.filter {
        it?.let {
            if(answers.gender == ""){
                it.city == answers.city  && it.tag.contains(answers.subject) && it.email != UserManager.user.email
            } else{
                it.city == answers.city && it.gender == answers.gender && it.tag.contains(answers.subject) && it.email != UserManager.user.email
            }

        }
    }
        ?: listOf()
}