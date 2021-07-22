package com.wenbin.knowhowbinding.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Following(
    val userEmail: String="",
    val userName: String=""
) : Parcelable