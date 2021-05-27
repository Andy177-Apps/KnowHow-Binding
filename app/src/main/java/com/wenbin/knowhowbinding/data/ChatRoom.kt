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

@Parcelize
data class Message(
        var id: String = "",
        var senderName : String = "",
        var senderImage : String = "",
        var senderEmail : String = "",
        var text : String = "",
        var createdTime: Long = 0L
) : Parcelable {
    @SuppressLint("SimpleDateFormat")
    @IgnoredOnParcel
    private val sdf = SimpleDateFormat("yyyy.MM.dd.HH.mm")
    val showTime = sdf.format(createdTime)
}

@Parcelize
data class UserInfo(
        var userEmail : String = "",
        var userName : String = "",
        var userImage : String = ""
): Parcelable