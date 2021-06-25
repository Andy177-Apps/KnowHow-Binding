package com.wenbin.knowhowbinding.ext

import com.wenbin.knowhowbinding.data.Answer
import com.wenbin.knowhowbinding.data.Event
import com.wenbin.knowhowbinding.data.User
import com.wenbin.knowhowbinding.login.UserManager
import com.wenbin.knowhowbinding.util.Logger

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

fun List<User>?.excludeOwner(ownerUserEmail: String) : List<User> {
    return this?.filter {
        it.email != ownerUserEmail
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
    Logger.d("resultList in SortExt= $resultList")

    return resultList
}

fun List<User>?.recommendedUser(ownerUser: User) : List<User> {
    val resultList = mutableListOf<User>()

    this?.let {
        for (user in this) {

            for (talentedSubject in user.talentedSubjects) {
                if (ownerUser.interestedSubjects.contains(talentedSubject)) {
                    resultList.add(user)
                }
            }

            for (interestedSubject in user.interestedSubjects) {
                if (ownerUser.talentedSubjects.contains(interestedSubject)) {
                    if (!resultList.contains(user)){
                        resultList.add(user)
                    }
                }
            }
        }
    }
    return resultList
}