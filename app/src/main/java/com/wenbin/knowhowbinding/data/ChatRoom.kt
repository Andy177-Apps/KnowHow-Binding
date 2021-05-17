package com.wenbin.knowhowbinding.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatRoom(
        var id : String = "",
        var latestTime : Long = -0L,
        var attendeesInfo : List<UserInfo> = listOf(),
        var attendees : List<String> = listOf(""),
        var attenderName: List<String> = emptyList(),
        var latestMessage : Message? = null
): Parcelable

@Parcelize
data class UserInfo(
        var userEmail : String = "",
        var userName : String = "",
        var userImage : String = ""
): Parcelable