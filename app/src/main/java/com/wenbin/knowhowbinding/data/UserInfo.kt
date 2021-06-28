package com.wenbin.knowhowbinding.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfo(
    var userEmail : String = "",
    var userName : String = "",
    var userImage : String = ""
): Parcelable