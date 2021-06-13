package com.wenbin.knowhowbinding.ext

import android.util.Log
import com.wenbin.knowhowbinding.data.Answer
import com.wenbin.knowhowbinding.data.Event
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.login.UserManager

fun List<Event>?.sortByTimeStamp (selectedTime: Long) : List<Event> {
    return this?.filter {
        it.let {
            selectedTime <= it.eventTime && it.eventTime < selectedTime + 86400000
        }
    }
        ?: listOf()
}

fun List<User>?.excludeOwner() : List<User> {
    return this?.filter {
        it.email != UserManager.user.email
    } ?: listOf()
}

fun List<User>?.sortByUserAnswer(answer : Answer) : List<User> {
    val resultList = mutableListOf<User>()

    var listFilteredByGender  = if (answer.gender != "unlimited") {
        this?.filter {
            it.gender == answer.gender
        } ?: listOf()
    } else {
        this!!
    }

    if (answer.type == "找老師") {
        for (subject in answer.subject) {
            for (item in listFilteredByGender) {
                if (item.talentedSubjects.contains(subject)) {
                    for (city in answer.city) {
                        if (item.city.contains(city)){
                            resultList.add(item)
                        }
                    }
                }
            }
        }
    } else {
        for (subject in answer.subject) {
            for (item in listFilteredByGender) {
                if (item.interestedSubjects.contains(subject)) {
                    for (city in answer.city) {
                        if (item.city.contains(city)){
                            resultList.add(item)
                        }
                    }
                }
            }
        }
    }
    Log.d("checkSearchList", "resultList in SortExt= $resultList")

    return resultList
}