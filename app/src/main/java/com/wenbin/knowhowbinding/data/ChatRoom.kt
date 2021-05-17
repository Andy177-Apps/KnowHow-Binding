package com.wenbin.knowhowbinding.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ChatRoom(
        var id : String = "",
        var latestTime : Long = -0L,
        var attendeesInfo : List<UserInfo> = listOf(),
        var attendees : List<String> = listOf(""),
        var attenderName: List<String> = emptyList(),
        var text : String = "",
        var mainImage : String = "",
        var message : Message? = null
): Parcelable
@Parcelize
data class Message(
        @Json(name = "user_id") val userId : String = "",
        var userName : String = "",
        var userImage : String? = null,
        var time: Date? = Calendar.getInstance().time,
        var text : String = ""
) : Parcelable

@Parcelize
data class UserInfo(
        var userEmail : String = "",
        var userName : String = "",
        var userImage : String = ""
): Parcelable