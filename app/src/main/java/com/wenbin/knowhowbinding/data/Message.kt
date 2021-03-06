package com.wenbin.knowhowbinding.data

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat

@Parcelize
data class Message(
    var id: String = "",
    var senderName: String = "",
    var senderImage: String = "",
    var senderEmail: String = "",
    var text: String = "",
    var createdTime: Long = 0L
) : Parcelable