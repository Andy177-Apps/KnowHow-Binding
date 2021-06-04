package com.wenbin.knowhowbinding.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event (
    var id: String = "",
    var city: String = "",
    var title: String = "",
    var description: String = "",
    var creatorName : String = "",
    var creatorImage  : String = "",
    var createdTime : Long = 0L,
    var attendees : List<String> = listOf(""),
    var attendeesName : List<String> = listOf(""),
    var attendeesImage : List<String> = listOf(""),
    val tag  : String = "",
    val eventTime : Long = -1L,
    var startTime : Long = -1L,
    val endTime : Long = -1L,
    var invitation : List<String> = listOf("")

    ) : Parcelable