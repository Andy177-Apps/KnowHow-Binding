package com.wenbin.knowhowbinding.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id : String = "",
    val name: String,
    val email: String = ""
//    val comment : Comment
) : Parcelable

@Parcelize
data class Comment(
    val poster_id : String,
    val description: String
) : Parcelable