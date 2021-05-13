package com.wenbin.knowhowbinding.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message(
    val city: String,
    val description: String,
    val words : List<Words>
) : Parcelable

@Parcelize
data class Words(
    @Json(name = "user_id") val userId : String,
    val content: String,
    @Json(name = "created_time") val createdTime: String
) : Parcelable