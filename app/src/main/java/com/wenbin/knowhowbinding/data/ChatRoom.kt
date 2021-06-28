package com.wenbin.knowhowbinding.data

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat

@Parcelize
data class ChatRoom(
        var id : String = "",
        var latestTime : Long = -0L,
        var attendeesInfo : List<UserInfo> = listOf(),
        var attendees : List<String> = listOf(""),
        var attenderName: List<String> = emptyList(),
        var text : String = "",
        var mainImage : String = "",
        var message : Message? = null,
        val latestMessage: String = ""
): Parcelable
