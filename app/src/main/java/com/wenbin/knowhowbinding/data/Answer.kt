package com.wenbin.knowhowbinding.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Answer(
    val type: String = "",
    val city: List<String> = listOf(),
    val gender: String = "",
    val subject: List<String> = listOf(),
) : Parcelable
