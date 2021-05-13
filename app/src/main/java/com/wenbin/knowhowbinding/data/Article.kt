package com.wenbin.knowhowbinding.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Article(
    @Json(name = "author_name") val authorName : String,
    val city: String,
    val description: String,
    val category : String
) : Parcelable