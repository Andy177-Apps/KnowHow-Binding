package com.wenbin.knowhowbinding.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatRoom(
        var chatRoomId : String = "",
        var latestTime : Long = -0L,
        var attendeesInfo : List<UserInfo> = listOf(),
        var attendees : List<String> = listOf(""),
        var latestMessage : String = ""
): Parcelable

@Parcelize
data class UserInfo(
        var userEmail : String = "",
        var userName : String = "",
        var userImage : String = ""
): Parcelable