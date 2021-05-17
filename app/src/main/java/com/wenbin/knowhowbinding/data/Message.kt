package com.wenbin.knowhowbinding.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Message(
    @Json(name = "user_id") val userId : String = "",
    var userName : String = "",
    var userImage : String? = null,
    var time: Date? = Calendar.getInstance().time,
    var text : String? = null
) : Parcelable
